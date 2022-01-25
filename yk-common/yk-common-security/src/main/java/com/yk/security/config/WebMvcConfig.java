package com.yk.security.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yk.security.convertor.BaseEnumConverterFactory;
import com.yk.security.convertor.LongDateConverter;
import com.yk.security.interceptor.HeaderInterceptor;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author ruoyi
 */
public class WebMvcConfig implements WebMvcConfigurer {

    /** 不需要拦截地址 */
    public static final String[] excludeUrls = {"/login", "/logout", "/refresh"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getHeaderInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns(excludeUrls)
            .order(-10);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new BaseEnumConverterFactory());
        registry.addConverter(new LongDateConverter());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        HttpMessageConverter<?> converter = converters.stream()
            .filter(e -> e instanceof MappingJackson2HttpMessageConverter)
            .findFirst()
            .orElse(null);
        if (converter != null) {
            MappingJackson2HttpMessageConverter jackson = (MappingJackson2HttpMessageConverter) converter;
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 修改时间格式
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            jackson.setObjectMapper(objectMapper);
        }
    }

    /**
     * 自定义请求头拦截器
     */
    public HeaderInterceptor getHeaderInterceptor() {
        return new HeaderInterceptor();
    }
}
