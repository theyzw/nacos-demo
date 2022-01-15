package com.yk.gateway.filter;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 打印请求/响应日志
 */
@Slf4j
@Component
public class LogFilter implements GlobalFilter, Ordered {

    private static final String REQUEST_ID = "request-id";
    private static final String REQUEST_TIME = "request-time";
    private static final String COST_TIME = "cost-time";
    private static final String URL = "url";
    private static final String CONTENT_TYPE = "content-type";
    private static final String METHOD = "method";
    private static final String HEAD = "head";
    private static final String COOKIES = "cookies";
    private static final String PARAMS = "params";
    private static final String BODY = "body";
    private static final String STATUS = "status";

    private static final List<String> PASSWORD_KEYS =
        Lists.newArrayList("passwd", "password", "oldPassword", "newPassword");

    private static final List<String> HEAD_NOT_INCLUDE =
        Arrays.asList("Accept", "Accept-Encoding", "Accept-Charset",
            "Accept-Language", "Connection",
            "Content-Encoding", "Content-Type", "Vary",
            "Cache-Control", "Cookie", "Host", "accept",
            "accept-encoding", "accept-charset",
            "accept-language", "connection",
            "content-encoding", "content-type", "vary",
            "cache-control", "cookie", "host",
            "Content-Length", "SLB-IP", "User-Agent",
            "Sec-Fetch-Dest", "Sec-Fetch-Mode",
            "Sec-Fetch-User", "Postman-Token",
            "X-Forwarded-Proto", "Sec-Fetch-Site", "SLB-ID",
            "sec-ch-ua", "X-Tag", "Transfer-Encoding", "Request-Origion", "sec-ch-ua-mobile",
            "sec-ch-ua-platform");

    /**
     * body最大打印长度
     */
    private static final int BODY_PRINT_LENGTH = 20000;

    private SerializerFeature[] features = new SerializerFeature[]{
        SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect,
        SerializerFeature.QuoteFieldNames, SerializerFeature.WriteMapNullValue};

    private final AtomicLong id = new AtomicLong(1);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpMethod httpMethod = exchange.getRequest().getMethod();
        // 只打印get和post
        if (httpMethod != HttpMethod.GET && httpMethod != HttpMethod.POST) {
            return chain.filter(exchange);
        }

        long requestId = id.getAndIncrement();
        long requestTime = System.currentTimeMillis();

        // 响应
        ServerHttpResponseDecorator decoratedResponse = decoratedResponse(exchange, requestId, requestTime);

        if (httpMethod == HttpMethod.GET) {
            requestLog(exchange.getRequest(), "", requestId, requestTime);
            return chain.filter(exchange.mutate().request(exchange.getRequest()).response(decoratedResponse).build());
        }

        // 获取用户传来的数据类型
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
            // json请求
            Mono<Object> modifiedBody = serverRequest.bodyToMono(Object.class)
                .flatMap(body -> {
                    requestLog(exchange.getRequest(), body, requestId, requestTime);
                    return Mono.just(body);
                });
            return getVoidMono(exchange, chain, Object.class, modifiedBody, decoratedResponse);
        } else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            // 表单请求
            Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                .flatMap(body -> {
                    requestLog(exchange.getRequest(), body, requestId, requestTime);
                    return Mono.just(body);
                });
            return getVoidMono(exchange, chain, String.class, modifiedBody, decoratedResponse);
        }

        return chain.filter(exchange.mutate().request(exchange.getRequest()).response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }

    /**
     * 参照 ModifyRequestBodyGatewayFilterFactory.java 截取的方法
     *
     * @param exchange
     * @param chain
     * @param outClass
     * @param modifiedBody
     * @return
     */
    private Mono<Void> getVoidMono(ServerWebExchange exchange, GatewayFilterChain chain, Class outClass,
                                   Mono<?> modifiedBody, ServerHttpResponseDecorator decoratedResponse) {
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());

        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);

        return bodyInserter.insert(outputMessage, new BodyInserterContext())
            .then(Mono.defer(() -> {
                ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public HttpHeaders getHeaders() {
                        long contentLength = headers.getContentLength();
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.putAll(super.getHeaders());
                        if (contentLength > 0) {
                            httpHeaders.setContentLength(contentLength);
                        } else {
                            // this causes a 'HTTP/1.1 411 Length Required' on httpbin.org
                            httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                        }
                        return httpHeaders;
                    }

                    @Override
                    public Flux<DataBuffer> getBody() {
                        return outputMessage.getBody();
                    }
                };

                return chain.filter(exchange.mutate().request(decorator).response(decoratedResponse).build());
            }));
    }

    /**
     * 记录到请求日志中去
     *
     * @param request request
     * @param body    请求的body内容
     */
    private void requestLog(ServerHttpRequest request, Object body, Long requestId, long requestTime) {
        // 打印参数
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put(REQUEST_ID, requestId);
        map.put(REQUEST_TIME, DateUtil.formatDateTime(new Date(requestTime)));

        // 记录要访问的url
        URI uri = request.getURI();
        map.put(URL, uri);

        map.put(METHOD, request.getMethod());
        String contentType = Optional.ofNullable(request.getHeaders().getContentType())
            .map(MimeType::toString)
            .orElse("");
        map.put(CONTENT_TYPE, contentType);

        // head
        Map<String, Object> headers = Maps.newHashMap();
        request.getHeaders().forEach((k, v) -> {
            if (!HEAD_NOT_INCLUDE.contains(k)) {
                headers.put(k, StringUtils.join(v, ","));
            }
        });
        map.put(HEAD, headers);

        // cookie
        Map<String, Object> cookies = Maps.newHashMap();
        request.getCookies().forEach((k, v) -> {
            cookies.put(k, StringUtils.join(v, ","));
        });
        map.put(COOKIES, headers);

        // body
        String str = (String) body;
        str = PASSWORD_KEYS.stream()
            .reduce(str, (result, next) -> result.replaceAll("(\"" + next + "\":\")(.*?)(\")",
                "$1******$3"));
        map.put(BODY, str);

        log.info("[request]{}", JSON.toJSONString(map, features));
    }

    /**
     * 打印响应
     *
     * @param exchange
     * @param requestId
     * @param requestTime
     * @return
     */
    private ServerHttpResponseDecorator decoratedResponse(ServerWebExchange exchange,
                                                          long requestId,
                                                          long requestTime) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        // 打印参数
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put(REQUEST_ID, requestId);

        return new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        // probably should reuse buffers
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        //释放掉内存
                        DataBufferUtils.release(dataBuffer);

                        long costTime = System.currentTimeMillis() - requestTime;
                        map.put(COST_TIME, costTime + "ms");

                        MediaType contentType = originalResponse.getHeaders().getContentType();
                        map.put(STATUS, originalResponse.getStatusCode().value());
                        map.put(CONTENT_TYPE, contentType.toString());
                        map.put(URL, exchange.getRequest().getURI());

                        String response = new String(content, Charset.forName("UTF-8"));

                        if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)
                            && StringUtils.isNotBlank(response)) {
                            if (response.length() >= BODY_PRINT_LENGTH) {
                                String left = StringUtils.left(response, BODY_PRINT_LENGTH) + "...";
                                map.put(BODY, left);
                            } else {
                                Object object = JSON.parse(response);
                                map.put(BODY, object);
                            }
                        }

                        StringBuilder sb = new StringBuilder("\n[response]");
                        map.forEach((k, v) -> {
                            sb.append(k).append("=").append(v).append(", ");
                        });
                        log.info(sb.toString());

                        byte[] uppedContent = response.getBytes();
                        return bufferFactory.wrap(uppedContent);
                    }));
                }

                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }
}