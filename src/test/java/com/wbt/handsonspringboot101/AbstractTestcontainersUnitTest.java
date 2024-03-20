package com.wbt.handsonspringboot101;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestcontainersUnitTest {
    @BeforeAll
    static void beforeAll() {
        final var flyway = Flyway
                .configure()
                .dataSource(postgresqlContainer.getJdbcUrl(), postgresqlContainer.getUsername(), postgresqlContainer.getPassword())
                .load();
        flyway.migrate();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("wbt-dao-unit-test")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    private static void registerDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    private static DataSource getDatasource() {
        return DataSourceBuilder.create()
                .driverClassName(postgresqlContainer.getDriverClassName())
                .url(postgresqlContainer.getJdbcUrl())
                .username(postgresqlContainer.getUsername())
                .password(postgresqlContainer.getPassword())
                .build();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDatasource());
    }

}
