package com.wbt.handsonspringboot101.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository(value = "JPA")
public class CustomerJpaDataAccessService implements CustomerDAO {
    private final CustomerRepository customerRepository;

    public CustomerJpaDataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Boolean save(CustomerRequest request) {
        this.customerRepository.save(new Customer(request.name(), request.email(), request.age()));
        return true;
    }

    @Override
    public List<CustomerResponse> fetchAll() {
        return this.customerRepository.findAll()
                .stream()
                .map(customer -> new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getAge()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerResponse> fetchCustomer(Long customerId) {
        return this.customerRepository.findById(customerId)
                .stream()
                .map(customer -> new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getAge()))
                .findFirst();
    }

    @Override
    public Boolean removeCustomer(Long customerId) {
        final var optionalCustomer = this.customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            this.customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateCustomer(Long customerId, CustomerRequest request) {
        final var optionalCustomer = this.customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            final var customer = optionalCustomer.get();
            customer.setName(request.name());
            customer.setEmail(request.email());
            customer.setAge(request.age());

            this.customerRepository.save(customer);
            return true;
        }
        return false;
    }
}
