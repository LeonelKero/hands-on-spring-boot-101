package com.wbt.handsonspringboot101.customer;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FakeDataService implements CustomerDAO {
    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        customers.add(new Customer(1L, "Alex", "alex@gmail.com", 21));
        customers.add(new Customer(2L, "tom", "tom@gmail.com", 31));
    }

    @Override
    public Boolean save(final CustomerRequest request) {
        return customers.add(new Customer(customers.size() + 1L, request.name(), request.email(), request.age()));
    }

    @Override
    public List<CustomerResponse> fetchAll() {
        return customers.stream()
                .map(customer -> new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getAge()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerResponse> fetchCustomer(final Long customerId) {
        return customers.stream().filter(customer -> Objects.equals(customer.getId(), customerId))
                .map(customer -> new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getAge()))
                .findFirst();
    }

    @Override
    public Boolean removeCustomer(final Long customerId) {
        final var optionalCustomer = this.fetchCustomer(customerId);
        if (optionalCustomer.isPresent()) {
            customers = customers.stream().filter(customer -> !Objects.equals(customer.getId(), customerId)).collect(Collectors.toList());
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateCustomer(final Long customerId, final CustomerRequest request) {
        if (this.fetchCustomer(customerId).isEmpty()) {
            return false; // if selected customer doesn't exist
        } else {

            // if it does exist, get it
            final var existingCustomer = customers.stream().filter(customer -> Objects.equals(customer.getId(), customerId))
                    .findFirst()
                    .get();
            // update fields
            existingCustomer.setName(request.name());
            existingCustomer.setEmail(request.email());
            existingCustomer.setAge(request.age());
            // remove the old one
            this.removeCustomer(customerId);
            // save the updated one, and return
            customers.add(existingCustomer);

            return true;
        }
    }
}
