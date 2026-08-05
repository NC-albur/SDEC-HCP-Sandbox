package com.example.springdummyapp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
public class PostgresBaseTest {

    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:18-alpine")
            .withDatabaseName("test")
            .withUsername("testUser")
            .withPassword("testPassword");

    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void setup() {
        postgres.start();

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(postgres.getJdbcUrl());
        dataSource.setUser(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @AfterAll
    static void tearDownContainer() {
        if (postgres != null) {
            postgres.close();
        }
    }
}
