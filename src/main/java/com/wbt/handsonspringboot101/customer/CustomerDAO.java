package com.wbt.handsonspringboot101.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    Boolean save(final CustomerRequest request);
    List<CustomerResponse> fetchAll();
    Optional<CustomerResponse> fetchCustomer(final Long customerId);
    Boolean removeCustomer(final Long customerId);
    Boolean updateCustomer(final Long customerId, final CustomerRequest request);
    Boolean isEmailAlreadyExist(final String email);
    Optional<CustomerResponse> fetchCutomerByEmail(final String email);
}
