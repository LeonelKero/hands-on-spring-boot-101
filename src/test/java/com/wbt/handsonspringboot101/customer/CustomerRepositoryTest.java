package com.wbt.handsonspringboot101.customer;

import com.wbt.handsonspringboot101.AbstractTestcontainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainersUnitTest {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        // GIVEN
        final var testEmail = UUID.randomUUID() + "-test.email@hotmail.com";
        final var customer = new Customer("meno-test", testEmail, 29);
        underTest.save(customer);
        // WHEN
        final var customerByEmail = underTest.existsCustomerByEmail(testEmail);
        // THEN
        assertThat(customerByEmail).isTrue();
    }

    @Test
    void existsCustomerByEmailReturnFalseWhenEmailNotPresent() {
        // GIVEN
        final var testEmail = UUID.randomUUID() + "-test.email@hotmail.com";
        // WHEN
        final var customerByEmail = underTest.existsCustomerByEmail(testEmail);
        // THEN
        assertThat(customerByEmail).isFalse();
    }

    @Test
    void existsCustomerById() {
        // GIVEN
        final var testEmail = UUID.randomUUID() + "-test.email@hotmail.com";
        final var customer = new Customer("meno-test", testEmail, 29);
        final var savedCustomer = underTest.save(customer);
        // WHEN
        final var existingCustomer = underTest.existsCustomerById(savedCustomer.getId());
        // THEN
        assertThat(existingCustomer).isTrue();
    }

    @Test
    void existsCustomerByIdReturnFalseWhenIdDoNotExist() {
        // GIVEN
        final var wrongId = -1L;
        // WHEN
        final var existingCustomer = underTest.existsCustomerById(wrongId);
        // THEN
        assertThat(existingCustomer).isFalse();
    }

    @Test
    void findByEmail() {
        // GIVEN
        final var testEmail = UUID.randomUUID() + "-test.email@hotmail.com";
        final var customer = new Customer("meno-test", testEmail, 29);
        underTest.save(customer);
        // WHEN
        final var fetchedUser = underTest.findByEmail(testEmail);
        // THEN
        assertThat(fetchedUser).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isNotNull();
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void findByEmailWillReturnEmptyIfCustomerWithEmailDoNotExist() {
        // GIVEN
        final var testEmail = UUID.randomUUID() + "-test.email@hotmail.com";
        // WHEN
        final var fetchedUser = underTest.findByEmail(testEmail);
        // THEN
        assertThat(fetchedUser).isEmpty();
    }
}