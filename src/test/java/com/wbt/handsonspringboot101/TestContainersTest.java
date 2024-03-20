package com.wbt.handsonspringboot101;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestContainersTest {
    @Container
    private static final PostgreSQLContainer<?> postgresalContainer =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("wbt-dao-unit-test")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @Test
    void canStartPostgresDB() {
        assertThat(postgresalContainer.isRunning()).isTrue();
        assertThat(postgresalContainer.isCreated()).isTrue();
    }
}
