package com.wbt.handsonspringboot101.customer;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record CustomerService(CustomerDAO customerDAO) {

    public List<CustomerResponse> fetchAll() {
        return this.customerDAO.fetchAll();
    }

    public Boolean save(final CustomerRequest customerRequest) {
        return this.customerDAO.save(customerRequest);
    }

    public Optional<CustomerResponse> fetchCustomer(final Long id) {
        return this.customerDAO.fetchCustomer(id);
    }

    public Boolean removeCustomer(final Long id) {
        return this.customerDAO.removeCustomer(id);
    }

    public Boolean updateCustomer(final Long id, final CustomerRequest request) {
        return this.customerDAO.updateCustomer(id, request);
    }
}
