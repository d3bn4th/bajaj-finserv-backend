package com.bajaj.webhookapp.service;

import com.bajaj.webhookapp.model.InitialRequest;
import com.bajaj.webhookapp.model.InitialResponse;
import com.bajaj.webhookapp.model.WebhookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WebhookService {

    private static final String INITIAL_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MutualFollowersService mutualFollowersService;

    @Autowired
    private NthLevelFollowersService nthLevelFollowersService;

    @Autowired
    private ApplicationContext context;

    @EventListener(ApplicationReadyEvent.class)
    public void processWebhookOnStartup() {
        try {
            // Step 1: Make the initial API call
            InitialResponse response = callInitialEndpoint();

            // Step 2: Determine which algorithm to use and solve it
            List<?> result = solveAlgorithm(response);

            // Step 3: Send the result to the webhook URL
            sendResultToWebhook(response.getWebhookUrl(), response.getAccessToken(), result);

            System.out.println("Webhook process completed successfully!");

            // Exit the application with success status code
            exitApplication(0);
        } catch (Exception e) {
            System.err.println("Error in webhook process: " + e.getMessage());
            e.printStackTrace();

            // Exit the application with error status code
            exitApplication(1);
        }
    }

    private void exitApplication(int code) {
        // Get the current Spring application
        SpringApplication.exit(context, () -> code);
    }

    private InitialResponse callInitialEndpoint() {
        System.out.println("Making initial API call to " + INITIAL_URL);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Update with your details
        InitialRequest request = new InitialRequest(
                "John Doe", // Replace with your name
                "REG12347", // Replace with your registration number
                "john@example.com" // Replace with your email
        );

        HttpEntity<InitialRequest> entity = new HttpEntity<>(request, headers);

        InitialResponse response = restTemplate.postForObject(
                INITIAL_URL,
                entity,
                InitialResponse.class);

        System.out.println("Received response: webhookUrl=" + response.getWebhookUrl());
        return response;
    }

    private List<?> solveAlgorithm(InitialResponse response) {
        // Determine which algorithm to use based on regNo
        // For demo purposes, we'll check the last two digits of regNo (7)
        // We're assuming 7 is odd, so Question 1 (Mutual Followers)

        if (response.getFollowers() != null) {
            System.out.println("Solving Mutual Followers problem...");
            return mutualFollowersService.findMutualFollowers(response.getFollowers());
        } else if (response.getUserId() != null && response.getN() != null) {
            System.out.println("Solving Nth-Level Followers problem...");
            return nthLevelFollowersService.findNthLevelFollowers(
                    response.getFollowers(),
                    response.getUserId(),
                    response.getN());
        } else {
            throw new IllegalArgumentException("Unknown problem type in response");
        }
    }

    @Retryable(value = { RestClientException.class }, maxAttempts = 4, backoff = @Backoff(delay = 1000, multiplier = 2))
    private void sendResultToWebhook(String webhookUrl, String accessToken, List<?> result) {
        System.out.println("Sending result to webhook URL: " + webhookUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        // Update with your details
        WebhookResponse response = new WebhookResponse(
                "John Doe", // Replace with your name
                "REG12347", // Replace with your registration number
                "john@example.com", // Replace with your email
                result);

        HttpEntity<WebhookResponse> entity = new HttpEntity<>(response, headers);

        restTemplate.postForObject(
                webhookUrl,
                entity,
                String.class);

        System.out.println("Result sent successfully!");
    }
}