package com.stay_a_programmer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${config.cookie.max-age}")
    private int cookieMaxAge;

    @Bean
    public CookieConfig cookieConfig() {
        return new CookieConfig(this.cookieMaxAge);
    }
}
