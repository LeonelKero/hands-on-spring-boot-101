package com.wbt.handsonspringboot101.customer;

import com.wbt.handsonspringboot101.exception.DuplicateResourcefoundException;
import com.wbt.handsonspringboot101.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(customerRepository.existsCustomerByEmail(testEmail)).thenReturn(false);
        // WHEN
        final var argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        underTest.save(customer);
        // THEN
        verify(customerRepository).save(argumentCaptor.capture()); // capture the passed element¬

        final var capturedCustomer = argumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.age());
    }

    @Test
    void whenSavingNewCustomerWithExistingEmailThroughDuplicateResourceFoundException() {
        // GIVEN
        final var testEmail = UUID.randomUUID() + "-test@outlook.com";
        final var customer = new CustomerRequest("mel", testEmail, 50);
        underTest.save(customer); // save first customer with that ID

        final var customer2 = new CustomerRequest("Sass", testEmail, 43);
        when(customerRepository.existsCustomerByEmail(testEmail)).thenReturn(true);
        // WHEN  // THEN
        // try to save new customer with the same Email
        assertThatThrownBy(() -> underTest.save(customer2))
                .isInstanceOf(DuplicateResourcefoundException.class).hasMessage("Email already taken");
        // make sure that the repository will never be called with any customer class to be saved
        //verify(customerRepository, never()).save(any(Customer.class));
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
    void removeCustomerIfCustomerExists() {
        // GIVEN
        final var customerId = 1L;
        when(customerRepository.existsCustomerById(customerId)).thenReturn(true);
        // WHEN
        final var result = underTest.removeCustomer(customerId);
        // THEN
        verify(customerRepository).deleteById(customerId);
        assertThat(result).isTrue();
    }

    @Test
    void removeCustomerThroughExceptionIfCustomerDoesNotExist() {
        // GIVEN
        final var customerId = 1L;
        when(customerRepository.existsCustomerById(customerId)).thenReturn(false);
        // WHEN // THEN
        assertThatThrownBy(() -> underTest.removeCustomer(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer resource with id [%s] not found".formatted(customerId));

        verify(customerRepository, never()).deleteById(any(Long.class));
    }

    @Test
    void updateCustomer() {
        // GIVEN
        final var someId = 1L;
        final var someExistingCustomer = new CustomerRequest("test-name", "test@mail.com", 44);
        final var existingCustomer = new Customer(someId, "mai", "mai@mail.com", 32);
        when(customerRepository.findById(someId)).thenReturn(Optional.of(existingCustomer));
        // WHEN
        final var result = underTest.updateCustomer(someId, someExistingCustomer);
        // THEN
        verify(customerRepository).save(any(Customer.class));
        assertThat(result).isTrue();
    }

    @Test
    void cannotUpdateCustomerIfCustomerDoesNotExist() {
        // GIVEN
        final var someId = 1L;
        final var someExistingCustomer = new CustomerRequest("test-name", "test@mail.com", 44);
        when(customerRepository.findById(someId)).thenReturn(Optional.empty());
        // WHEN
        final var result = underTest.updateCustomer(someId, someExistingCustomer);
        // THEN
        assertThat(result).isFalse();
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