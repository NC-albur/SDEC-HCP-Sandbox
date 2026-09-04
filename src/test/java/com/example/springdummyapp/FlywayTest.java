package com.example.springdummyapp;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FlywayTest extends PostgresBaseTest {

    private static Flyway flyway;

    @BeforeAll
    static void setupContainer() {
        flyway = Flyway.configure()
                .dataSource(
                        postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword())
                .load();
    }

    @Test
    public void test() {

        List<String> tablesBeforeMigration = jdbcTemplate.queryForList(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'", String.class);

        assertEquals(0, tablesBeforeMigration.size());

        MigrateResult result = flyway.migrate();

        Integer threadCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM THREAD", Integer.class);
        List<String> tablesAfterMigration = jdbcTemplate.queryForList(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'", String.class);

        assertEquals(3, tablesAfterMigration.size());

        assertEquals(2, result.getSuccessfulMigrations().size());
        assertEquals(2, threadCount);
    }

}

