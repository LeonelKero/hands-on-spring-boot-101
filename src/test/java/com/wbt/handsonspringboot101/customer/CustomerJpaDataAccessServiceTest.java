package com.wbt.handsonspringboot101.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class CustomerJpaDataAccessServiceTest {

    private CustomerJpaDataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void save() {
        // GIVEN
        final var testEmail = UUID.randomUUID() + "-test@outlook.com";
        final var customer = new CustomerRequest("mel", testEmail, 50);
        // WHEN
        underTest.save(customer);
        // THEN
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void whenSavingNewCustomerWithExistingEmailThroughDuplicateResourceFoundException() {
        // GIVEN
        final var testEmail = UUID.randomUUID() + "-test@outlook.com";
        final var customer = new CustomerRequest("mel", testEmail, 50);
        underTest.save(customer);
        final var customer2 = new CustomerRequest("Sass", testEmail, 43);
        // WHEN
        underTest.save(customer2);
        // THEN

    }

    @Test
    void fetchAll() {
        // GIVEN
        // WHEN
        underTest.fetchAll();
        // THEN
        verify(customerRepository).findAll();
    }

    @Test
    void fetchCustomer() {
        // GIVEN
        Long customerId = 100L;
        // WHEN
        underTest.fetchCustomer(customerId);
        // THEN
        verify(customerRepository).findById(customerId);
    }

    @Test
    void removeCustomer() {
        // GIVEN
        final var customerId = 3L;
        // WHEN
        underTest.removeCustomer(customerId);
        // THEN
        verify(customerRepository).deleteById(customerId);
        // TODO: Fix this test - check use cases
    }

    @Test
    void updateCustomer() {
        // GIVEN
        final var someId = 1L;
        final var someExistingCustomer = new CustomerRequest("test-name", "test@mail.com", 44);
        // WHEN
        underTest.updateCustomer(someId, someExistingCustomer);
        // THEN
        verify(customerRepository).findById(someId);
        // TODO: Fix this test - check use cases
    }

    @Test
    void isEmailAlreadyExist() {
        // GIVEN
        final var someEmail = "test.project@mymail.com";
        // WHEN
        underTest.isEmailAlreadyExist(someEmail);
        // THEN
        verify(customerRepository).existsCustomerByEmail(someEmail);
    }

    @Test
    void fetchCustomerByEmail() {
        // GIVEN
        final var someEmail = "test.project@mymail.com";
        // WHEN
        underTest.fetchCustomerByEmail(someEmail);
        // THEN
        verify(customerRepository).findByEmail(someEmail);
    }
}