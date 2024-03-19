package com.wbt.handsonspringboot101.customer;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomerRowMapper implements RowMapper<CustomerResponse> {
    @Override
    public CustomerResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CustomerResponse(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age"));
    }
}
