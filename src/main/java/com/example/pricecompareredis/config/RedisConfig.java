package com.example.pricecompareredis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    // 커넥션 팩토리를 사용할 템플릿을 만들것이다.
    // 이미 셋팅 되어있는 정보와 옵션으로 레디스에 접근하기 위한 목적이다.
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키를 직렬화
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // 값도 직렬화
        return redisTemplate;
    }
    // 직렬화:
    // Redis는 네이티브로 문자열과 바이너리 데이터를 저장하고 처리한다.
    // Java 객체는 기본적으로 직렬화되지 않은 상태에서는 Redis에 저장할 수 없다.
    // 예를 들어, Java에서 HashMap 객체를 그대로 저장하려고하면 Redis는 이를 이해하지 못한다.
    // 데이터를 Redis가 이해할 수 있는 형식(문자열 또는 바이트 배열)으로 변환해야 한다.
    // 직렬화는 Java 객체를 바이트 스트림으로 변환하여 Redis에 저장할 수 있도록 도와준다.
    // 역직렬화:
    // Redis에서 데이터를 읽어올 때 저장된 데이터를 다시 Java 객체로 복원해야한다.
}
