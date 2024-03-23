package com.wbt.handsonspringboot101.journey;

import com.wbt.handsonspringboot101.customer.CustomerRequest;
import com.wbt.handsonspringboot101.customer.CustomerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String URI = "/api/v1/customers";

    @Test
    void customerJourney() {
        // Forge new customer
        final String name = "mansah";
        final String email = "mansah@hotmail.com";
        final int age = 45;
        final var request = new CustomerRequest(name, email, age);

        // Save new customer
        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRequest.class)
                .exchange()
                .expectStatus().isCreated();

        // Get all customers
        final var customers = webTestClient
                .get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerResponse>() {
                })
                .returnResult()
                .getResponseBody();
        // Make sure a customer is present
        final var expectedCustomer = new CustomerResponse(null, name, email, age); // because we don't the value of ID field
        assertThat(customers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);
    }
}
