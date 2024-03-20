package com.wbt.handsonspringboot101;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class TestContainersTest extends AbstractTestcontainersUnitTest {

    @Test
    void canStartPostgresDB() {
        assertThat(postgresqlContainer.isRunning()).isTrue();
        assertThat(postgresqlContainer.isCreated()).isTrue();
    }
}
