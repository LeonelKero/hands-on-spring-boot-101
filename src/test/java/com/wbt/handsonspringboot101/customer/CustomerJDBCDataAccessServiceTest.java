package com.wbt.handsonspringboot101.customer;

import com.wbt.handsonspringboot101.AbstractTestcontainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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
        String testEmail = "nero-" + UUID.randomUUID() + ".test@gmail.com";
        final var customer = new CustomerRequest("nero-test", testEmail, 20);
        // WHEN
        underTest.save(customer);
        final var savedCustomer = underTest.isEmailAlreadyExist(testEmail);
        // THEN
        assertThat(savedCustomer).isTrue();
    }

    @Test
    void fetchAll() {
        // GIVEN
        String testEmail = "nero-" + UUID.randomUUID() + ".test@gmail.com";
        final var customer = new CustomerRequest("nero-test", testEmail, 20);
        // WHEN
        underTest.save(customer);
        final var customers = underTest.fetchAll();
        // THEN
        assertThat(customers.size()).isGreaterThan(0);
    }

    @Test
    void fetchCustomer() {
        // GIVEN
        String testEmail = "nero-" + UUID.randomUUID() + ".test@gmail.com";
        final var customer = new CustomerRequest("nero-test", testEmail, 27);
        underTest.save(customer);
        // WHEN
        final var savedCustomer = underTest.fetchCutomerByEmail(testEmail);
        final var id = underTest.fetchCustomer(savedCustomer.get().id()).orElseThrow().id();
        // THEN
        assertThat(savedCustomer).isNotEmpty();
        assertThat(savedCustomer).isPresent().hasValueSatisfying(customerResponse -> {
            assertThat(customerResponse.id()).isEqualTo(id);
            assertThat(customerResponse.name()).isEqualTo(savedCustomer.get().name());
            assertThat(customerResponse.email()).isEqualTo(savedCustomer.get().email());
            assertThat(customerResponse.age()).isEqualTo(savedCustomer.get().age());
        });
    }

    @Test
    void removeCustomer() {
        // GIVEN
        String testEmail = "nero-" + UUID.randomUUID() + ".test@gmail.com";
        final var customer = new CustomerRequest("nero-test", testEmail, 20);
        // WHEN
        underTest.save(customer);
        final var savedCustomer = underTest.fetchCutomerByEmail(testEmail).orElseThrow();
        final var isRemoved = underTest.removeCustomer(savedCustomer.id());
        // THEN
        assertThat(isRemoved).isTrue();
    }

    @Test
    void whenCustomerDoNotExistByIdNotDeletion() {
        // GIVEN
        final var fakeCustomerId = -1L;
        // WHEN
        final var result = underTest.removeCustomer(fakeCustomerId);
        // THEN
        assertThat(result).isFalse();
    }

    @Test
    void whenGivenWrongCustomerIdReturnEmpty() {
        // GIVEN
        final var wrongId = -2L;
        // WHEN
        final var possibleCustomer = underTest.fetchCustomer(wrongId);
        // THEN
        assertThat(possibleCustomer).isEmpty();
    }

    @Test
    void updateCustomer() {
        // GIVEN
        String testEmail = "nero-" + UUID.randomUUID() + ".test@gmail.com";
        final var customer = new CustomerRequest("mel-test", testEmail, 28);
        // WHEN
        underTest.save(customer);
        final var savedCustomer = underTest.fetchCutomerByEmail(testEmail).orElseThrow();
        String newEmail = UUID.randomUUID() + ".mel@hotmail.com";
        final var newCustomer = new CustomerRequest("mel-new-name", newEmail, 28);
        final var isUpdated = underTest.updateCustomer(savedCustomer.id(), newCustomer);
        // THEN
        assertThat(underTest.fetchCutomerByEmail(testEmail)).isEmpty();
        assertThat(isUpdated).isTrue();
        assertThat(underTest.fetchCutomerByEmail(newEmail)).isPresent().hasValueSatisfying(customerResponse -> {
            assertThat(customerResponse.name()).isEqualTo(newCustomer.name());
            assertThat(customerResponse.email()).isEqualTo(newCustomer.email());
            assertThat(customerResponse.age()).isEqualTo(newCustomer.age());
        });
    }

    @Test
    void isEmailAlreadyExist() {
        // GIVEN
        String testEmail = "nero-" + UUID.randomUUID() + ".test@gmail.com";
        final var customer = new CustomerRequest("nero-test", testEmail, 20);
        final var customer2 = new CustomerRequest("mel-test", testEmail, 32);
        // WHEN
        underTest.save(customer);
        // THEN
        assertThat(underTest.save(customer2)).isFalse();
    }

    @Test
    void requestCustomerWithNullEmailValueReturnEmpty() {
        // GIVEN // WHEN
        final var result = underTest.fetchCutomerByEmail(null);
        // THEN
        assertThat(result).isEmpty();
    }
}