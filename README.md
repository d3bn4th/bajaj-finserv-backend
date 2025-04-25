# Spring Boot Webhook Application

This Spring Boot application automatically interacts with a remote API at startup, solves an algorithmic problem, and sends the result to a webhook URL.

## Features

- Automatically calls the `/generateWebhook` endpoint when the application starts
- Solves one of two algorithmic problems:
  - Mutual Followers: Find pairs of users who follow each other
  - Nth-Level Followers: Find users exactly N levels away from a starting user
- Stores the result in JSON format
- Sends the result to a provided webhook URL with JWT authentication
- Implements a retry policy for the webhook call

## Prerequisites

- Java 11+
- Maven 3.6+

## Configuration

Update the following fields in `WebhookService.java` with your information:

```java
InitialRequest request = new InitialRequest(
    "YourName",            // Replace with your name
    "YourRegNo",           // Replace with your registration number
    "your@email.com"       // Replace with your email
);
```

And also in the webhook response:

```java
WebhookResponse response = new WebhookResponse(
    "YourName",            // Replace with your name
    "YourRegNo",           // Replace with your registration number
    "your@email.com",      // Replace with your email
    result
);
```

## Building the Application

```bash
mvn clean package
```

This will create a JAR file in the `target` directory.

## Running the Application

```bash
java -jar target/webhook-app-0.0.1-SNAPSHOT.jar
```

The application will automatically:
1. Call the initial API endpoint
2. Solve the assigned problem
3. Send the result to the webhook URL

## Implementation Details

- The application uses Spring Boot's `ApplicationReadyEvent` to trigger the webhook process at startup
- RestTemplate is used for making HTTP requests
- Spring Retry is used to implement the retry policy for the webhook call
- The algorithm solutions are implemented in separate service classes 