package com.marcosbarbero.storefront.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marcosbarbero.storefront.config.BookingProperties;
import com.marcosbarbero.storefront.dto.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcos Barbero
 */
@Slf4j
@Service
public class AsyncCustomerRegistration {

    private final RestTemplate restTemplate;

    private final BookingProperties bookingProperties;

    @Autowired
    public AsyncCustomerRegistration(final RestTemplate restTemplate, BookingProperties bookingProperties) {
        this.restTemplate = restTemplate;
        this.bookingProperties = bookingProperties;
    }

    /**
     * Build a CustomerDTO from UserProfile.
     *
     * @param userProfile The UserProfile
     * @return The CustomerDTO
     */
    private CustomerDTO build(UserProfile userProfile) {
        final CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFullName(userProfile.getName());
        customerDTO.setUsername(userProfile.getUsername());
        return customerDTO;
    }

    /**
     * Verify is the customer is already in our database.
     *
     * @param username The username.
     * @return boolean
     */
    private boolean found(final String username) {
        String[] args = {username};
        ResponseEntity<String> exchange = restTemplate.exchange(this.bookingProperties.getCustomers() + "/username/{username}",
                HttpMethod.GET, new HttpEntity<>((Void) null),
                String.class, args);
        log.info("The username: '{}' returned statusCode: '{}'", username, exchange.getStatusCode());
        return exchange.getStatusCode().is2xxSuccessful();
    }

    /**
     * Save the user.
     *
     * @param userProfile UserProfile
     */
    private void post(final UserProfile userProfile) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        final HttpEntity<CustomerDTO> entity = new HttpEntity<>(build(userProfile), headers);
        this.restTemplate.exchange(this.bookingProperties.getCustomers(), HttpMethod.POST, entity, String.class);
        log.info("Customer registered: {}", userProfile.getName());
    }

    /**
     * Async method to save user
     *
     * @param userProfile The UserProfile
     * @throws JsonProcessingException
     */
    @Async
    public void execute(final UserProfile userProfile) throws JsonProcessingException {
        if (!found(userProfile.getUsername())) {
            this.post(userProfile);
        }
    }
}
