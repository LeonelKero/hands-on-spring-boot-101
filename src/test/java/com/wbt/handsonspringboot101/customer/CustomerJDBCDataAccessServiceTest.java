package com.wbt.handsonspringboot101.customer;

import com.wbt.handsonspringboot101.AbstractTestcontainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainersUnitTest {

    private final CustomerRowMapper rowMapper = new CustomerRowMapper();
    private CustomerJDBCDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), rowMapper);
    }

    @Test
    void save() {
        // GIVEN
        final var customer = new CustomerRequest("nero-test", "nero.test@gmail.com", 20);
        // WHEN
        underTest.save(customer);
        List<CustomerResponse> customerResponseList = underTest.fetchAll();
        // THEN
        assertThat(customerResponseList).isNotEmpty();
    }

    @Test
    void fetchAll() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    void fetchCustomer() {
        // GIVEN
        String testEmail = "nero.test@gmail.com";
        final var customer = new CustomerRequest("nero-test", testEmail, 20);
        underTest.save(customer);
        // WHEN
        final var savedCustomer = underTest.fetchAll()
                .stream()
                .filter(customerResponse -> customerResponse.email().equals(testEmail))
                .findFirst();
        // THEN
        assertThat(savedCustomer).isNotEmpty();
        assertThat(savedCustomer.get().name()).isEqualTo("nero-test");
    }

    @Test
    void removeCustomer() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    void updateCustomer() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    void isEmailAlreadyExist() {
        // GIVEN

        // WHEN

        // THEN
    }
}