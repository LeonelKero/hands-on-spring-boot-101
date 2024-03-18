package com.wbt.handsonspringboot101.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/api/v1/customers"})
public record CustomerController(FakeDataService fakeDataService) {

    @GetMapping
    ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok(this.fakeDataService.fetchAll());
    }

    @GetMapping(path = {"/{id}"})
    ResponseEntity<CustomerResponse> getCustomer(final @PathVariable(name = "id") Long id) {
        final var optionalCustomer = this.fakeDataService.fetchCustomer(id);
        return optionalCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<String> add(final @RequestBody CustomerRequest customerRequest) {
        if (this.fakeDataService.save(customerRequest))
            return new ResponseEntity<>("Customer resource saved successfully", HttpStatus.CREATED);
        return new ResponseEntity<>("Unable to save this resource", HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = {"/{id}"})
    ResponseEntity<String> update(final @PathVariable(name = "id") Long id, final @RequestBody CustomerRequest customerRequest) {
        if (this.fakeDataService().updateCustomer(id, customerRequest))
            return ResponseEntity.ok("Customer with id %s resource updated successfully".formatted(id));
        return new ResponseEntity<>("Unable to update this resource", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = {"/{id}"})
    ResponseEntity<String> delete(final @PathVariable(name = "id") Long id) {
        if (this.fakeDataService.removeCustomer(id))
            return ResponseEntity.ok("Customer with id %s resource removed successfully".formatted(id));
        return new ResponseEntity<>("Unable to delete this resource, may not exist", HttpStatus.BAD_REQUEST);
    }

}
