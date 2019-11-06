package com.apps.potok.soketio.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

//@EnableSpringHttpSession
@Configuration
public class SessionConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    public SessionConfig(ObjectProvider<RedisConnectionFactory> redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory.getIfAvailable();
    }

    @Bean
    public RedisOperations<Object, Object> sessionRedisOperations() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(this.redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisOperationsSessionRepository sessionRepository(RedisOperations<Object, Object> sessionRedisOperations) {
        return new RedisOperationsSessionRepository(sessionRedisOperations);
    }

}
