package com.wbt.handsonspringboot101.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Boolean existsCustomerByEmail(final String email);

    Boolean existsCustomerById(final Long id);

    Optional<Customer> findByEmail(final String customerEmail);
}
