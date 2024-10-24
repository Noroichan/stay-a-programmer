package com.stay_a_programmer.config;

import com.stay_a_programmer.dto.CartItemDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration("localhost", 6379);
        redisConfig.setPassword("LetMeIn");
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, List<CartItemDTO>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<CartItemDTO>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
