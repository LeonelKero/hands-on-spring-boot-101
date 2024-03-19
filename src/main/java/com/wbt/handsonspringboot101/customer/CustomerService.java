package com.wbt.handsonspringboot101.customer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record CustomerService(@Qualifier(value = "JPA") CustomerDAO customerDAO) {

    public List<CustomerResponse> fetchAll() {
        return this.customerDAO.fetchAll();
    }

    public Boolean save(final CustomerRequest customerRequest) {
        return this.customerDAO.save(customerRequest);
    }

    public ResponseEntity<CustomerResponse> fetchCustomer(final Long id) {
        return this.customerDAO.fetchCustomer(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public Boolean removeCustomer(final Long id) {
        return this.customerDAO.removeCustomer(id);
    }

    public Boolean updateCustomer(final Long id, final CustomerRequest request) {
        return this.customerDAO.updateCustomer(id, request);
    }
}
