package com.example.springdummyapp.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

    private final DataSource dataSource;

    @Bean
    public Flyway flywayMigrate() {
         return Flyway.configure()
                .dataSource(dataSource)
                .load();
    }
}
