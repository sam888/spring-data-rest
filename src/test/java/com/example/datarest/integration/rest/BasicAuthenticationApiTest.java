package com.example.datarest.integration.rest;

import com.example.datarest.web.handler.BasicAuthenticationHandler;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Verify Basic Authentication is working as expected.
 *
 * Since this is integration test, Spring Boot needs to be running before running these tests => all tests are
 * disabled by default, uncomment @Disable below to run tests after the Spring Boot app is up and running.
 *
 */
@Disabled
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)    // Allows init(..) to be used as non-static method
public class BasicAuthenticationApiTest {

    String localhost = "http://localhost:8080/test";

    // The 8080 port here must match the running Spring Boot app
    private String basicAuthUrl = localhost + "/basic-auth";
    private String welcomeUrl = localhost + "/welcome";

    private HttpEntity<String> badCredentialHttpEntity;
    private HttpEntity<String> goodCredentialHttpEntity;

    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @BeforeAll
    public void init() {
        restTemplate = restTemplateBuilder.build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("myUsername", "myPassword");
        badCredentialHttpEntity = new HttpEntity<String>(headers);

        headers = new HttpHeaders();
        headers.setBasicAuth(BasicAuthenticationHandler.VALID_USERNAME, BasicAuthenticationHandler.VALID_PASSWORD);
        goodCredentialHttpEntity = new HttpEntity<String>(headers);
    }


    /**
     * Verify basic authentication fails as expected with the wrong credential for rest
     * end point /basic-auth
     */
    @Test
    void test_basicAuthEndpoint_failsAsExpected() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.exchange(basicAuthUrl, HttpMethod.GET, badCredentialHttpEntity, String.class);
        });

        assertTrue( exception.getStatusCode() == HttpStatus.UNAUTHORIZED );
    }

    /**
     * Verify successful basic authentication given the right credential for rest end point /basic-auth
     */
    @Test
    void test_basicAuthEndpoint_success() {
        ResponseEntity<String> response = restTemplate.exchange(basicAuthUrl, HttpMethod.GET,
            goodCredentialHttpEntity, String.class);

        System.out.println("response: " + response.getBody());
        assertTrue( response.getStatusCode() == HttpStatus.OK );
    }

    /**
     * Verify basic authentication annotation @BasicAuthentication works as expected for endpoint
     * /welcome/{pathVariable} using @PathVariable with non-empty path variable {pathVariable}
     */
    @Test
    void test_welcomeEndPoint_withParameter_success() {
        ResponseEntity<String> response = restTemplate.exchange(welcomeUrl + "/world", HttpMethod.GET,
            goodCredentialHttpEntity, String.class);

        System.out.println("response: " + response.getBody());
        assertTrue( response.getStatusCode() == HttpStatus.OK );
        assertEquals( "Hello world", response.getBody() );
    }

    /**
     * Verify basic authentication annotation @BasicAuthentication works as expected for endpoint /welcome
     * using @PathVariable with empty path variable
     */
    @Test
    void test_welcomeEndPoint_withEmptyParameter_success() {
        ResponseEntity<String> response = restTemplate.exchange(welcomeUrl, HttpMethod.GET,
                goodCredentialHttpEntity, String.class);

        System.out.println("response: " + response.getBody());
        assertTrue( response.getStatusCode() == HttpStatus.OK );
        assertEquals( "Hello null", response.getBody() );
    }

}
