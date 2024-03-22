package com.wbt.handsonspringboot101.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "JDBC")
public class CustomerJDBCDataAccessService implements CustomerDAO {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public Boolean save(final CustomerRequest request) {
        final var insertSql = """
                INSERT INTO customer(name, email, age) VALUES (?, ?, ?)
                """;
        if (!this.isEmailAlreadyExist(request.email())) {
            final var rowsAffected = this.jdbcTemplate.update(insertSql, request.name(), request.email(), request.age());
            return rowsAffected != 0;
        }
        return false;
    }

    @Override
    public List<CustomerResponse> fetchAll() {
        return this.jdbcTemplate.query("SELECT * FROM customer", customerRowMapper);
    }

    @Override
    public Optional<CustomerResponse> fetchCustomer(final Long customerId) {
        final var selectedCustomer = this.jdbcTemplate.query("SELECT * FROM customer WHERE id = ?", customerRowMapper, customerId);
        return selectedCustomer.stream().findFirst();
    }

    @Override
    public Boolean removeCustomer(final Long customerId) {
        if (this.fetchCustomer(customerId).isEmpty()) return false;
        final var deleteSql = "DELETE FROM customer WHERE id = ?";
        final var rowsAffected = this.jdbcTemplate.update(deleteSql, customerId);
        return rowsAffected != 0;
    }

    @Override
    public Boolean updateCustomer(final Long customerId, final CustomerRequest request) {
        final var updatedRows = this.jdbcTemplate.update(
                "UPDATE customer SET name = ?, email = ?, age = ? WHERE id = ?",
                request.name(), request.email(), request.age(), customerId);
        return updatedRows != 0;
    }

    @Override
    public Boolean isEmailAlreadyExist(final String email) {
        /*
        final var count = this.jdbcTemplate.queryForObject("select count(id) from customer where email = ?", Integer.class, email);
        return count != null && count > 0;
        */
        final var result = this.jdbcTemplate.query("SELECT id, name, email, age FROM customer WHERE email = ?", customerRowMapper, email);
        return !result.isEmpty();
    }

    @Override
    public Optional<CustomerResponse> fetchCutomerByEmail(final String email) {
        if (email != null) {
            final var selectSql = "SELECT id, name, email, age FROM customer WHERE email = ?";
            final var customer = this.jdbcTemplate.query(selectSql, customerRowMapper, email);
            return customer.stream().findFirst();
        }
        return Optional.empty();
    }
}
