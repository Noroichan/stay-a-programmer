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

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.driver-class}")
    private String dbDriver;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean
    public PostgresConfig postgresConfig() {
        return new PostgresConfig(dbUrl, dbDriver, dbUser, dbPassword);
    }
}
