package com.wbt.handsonspringboot101.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // GIVEN
        final var wrapper = new CustomerRowMapper();
        final var resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("Test user");
        when(resultSet.getString("email")).thenReturn("test.user@hotmail.com");
        when(resultSet.getInt("age")).thenReturn(20);

        // WHEN
        final var actual = wrapper.mapRow(resultSet, 1);

        // THEN
        final var expected = new Customer(1L, "Test user", "test.user@hotmail.com", 20);
        assertThat(actual).isNotNull();
        assertThat(actual.name()).isEqualTo(expected.getName());
    }
}