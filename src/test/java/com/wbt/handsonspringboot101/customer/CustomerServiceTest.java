package com.wbt.handsonspringboot101.customer;

import com.wbt.handsonspringboot101.exception.DuplicateResourcefoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void fetchAll() {
        // GIVEN // WHEN
        underTest.fetchAll();
        // THEN
        verify(customerDAO).fetchAll();
    }

    @Test
    void save() {
        // GIVEN
        final var id = 3L;
        final var customer = new CustomerRequest("mel", "mel@hotmail.com", 33);
        when(customerDAO.isEmailAlreadyExist(customer.email())).thenReturn(false);
        when(customerDAO.save(customer)).thenReturn(true);
        // WHEN
        final var result = underTest.save(customer);
        // THEN
        verify(customerDAO).save(customer);
        assertThat(result).isTrue();
    }

    @Test
    void savingCustomerWithExistingEmailThroughException() {
        // GIVEN
        final var id = 3L;
        final var customer = new CustomerRequest("mel", "already.use@hotmail.com", 33);
        when(customerDAO.isEmailAlreadyExist(customer.email())).thenReturn(true);
        // WHEN
        // THEN
        assertThatThrownBy(() -> underTest.save(customer)).isInstanceOf(DuplicateResourcefoundException.class);
    }

    @Test
    void fetchCustomer() {
        // GIVEN
        final var someId = 2L;
        // WHEN
        underTest.fetchCustomer(someId);
        // THEN
        verify(customerDAO).fetchCustomer(someId);
    }

    @Test
    void removeCustomer() {
        // GIVEN
        final var customerId = 5L;
        // WHEN
        underTest.removeCustomer(customerId);
        // THEN
        verify(customerDAO).removeCustomer(customerId);
    }

    @Test
    void updateCustomer() {
        // GIVEN
        final var someId = 2L;
        final var someCustomerData = new CustomerRequest("user name", "user@mail.com", 90);
        // WHEN
        underTest.updateCustomer(someId, someCustomerData);
        // THEN
        verify(customerDAO).updateCustomer(someId, someCustomerData);
    }
}