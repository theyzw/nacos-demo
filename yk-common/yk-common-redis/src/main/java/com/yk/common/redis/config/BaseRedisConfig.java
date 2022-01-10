package com.yk.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.yk.common.redis.utils.RedisUtil;
import java.time.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置
 *
 * @author yzw
 */
@Configuration
@EnableCaching
public class BaseRedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.database:0}")
    protected Integer database;
    @Value("${spring.redis.host:127.0.0.1}")
    protected String host;
    @Value("${spring.redis.port:6379}")
    protected Integer port;
    @Value("${spring.redis.password:}")
    protected String password;
    @Value("${spring.redis.lettuce.pool.max-active:100}")
    protected Integer maxActive;
    @Value("${spring.redis.lettuce.pool.max-wait:1000}")
    protected Integer maxWait;
    @Value("${spring.redis.lettuce.pool.max-idle:50}")
    protected Integer maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle:0}")
    protected Integer minIdle;
    @Value("${spring.redis.lettuce.shutdown-timeout:0}")
    protected Integer timeout;

    @Bean
    public RedisUtil redisUtil() {
        return new RedisUtil();
    }

    @Bean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        return getTemplate(redisConnectionFactory());
    }

    protected RedisConnectionFactory redisConnectionFactory() {
        return connectionFactory(maxActive, maxIdle, minIdle, maxWait, host, password, timeout, port, database);
    }

    protected RedisConnectionFactory connectionFactory(Integer maxActive,
                                                       Integer maxIdle,
                                                       Integer minIdle,
                                                       Integer maxWait,
                                                       String host,
                                                       String password,
                                                       Integer timeout,
                                                       Integer port,
                                                       Integer database) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);
        if (StringUtils.isNoneBlank(password)) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        }

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        LettuceClientConfiguration lettucePoolingConfig =
            LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .shutdownTimeout(Duration.ofMillis(timeout))
                .build();
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration,
            lettucePoolingConfig);
        connectionFactory.afterPropertiesSet();

        return connectionFactory;
    }

    private RedisTemplate<String, Object> getTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = stringRedisSerializer();

        // key string序列化
        template.setKeySerializer(stringRedisSerializer);
        // value jackson序列化
        template.setValueSerializer(jackson2JsonRedisSerializer());
        // hash field string序列化
        template.setHashKeySerializer(stringRedisSerializer);
        // hash value jackson序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer());

        template.afterPropertiesSet();

        return template;
    }

    protected StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    protected RedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
            new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // 反序列化时忽略多余字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

}