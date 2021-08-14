package com.example.datarest.web.handler;

import com.example.datarest.exception.AuthenticationException;
import com.example.datarest.exception.BasicAuthenticationException;
import com.example.datarest.model.Person;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Base64;

@Component
public class BasicAuthenticationHandler {

    // Testing credential only. Query database for actual username & password for production.
    public static String VALID_USERNAME = "JohnDoe";
    public static String VALID_PASSWORD = "1234";

    public Person getPerson(NativeWebRequest nativeWebRequest) throws AuthenticationException {
        try {
            // Retrieving credentials the HTTP Authorization Header
            String authorizationCredentials = nativeWebRequest.getHeader(HttpHeaders.AUTHORIZATION)
                .substring("Basic".length()).trim();

            // decoding credentials
            String[] decodedCredentials = new String( Base64.getDecoder().decode(
               authorizationCredentials) ).split(":");

            System.out.println("decodedCredentials: username=" + decodedCredentials[0] +
                ", password=" + decodedCredentials[1]);

            if ( authenticate( decodedCredentials ) ) {
                return new Person();
            }

            throw new BasicAuthenticationException();
        } catch (Exception e) {
            throw new BasicAuthenticationException( e.getCause() );
        }
    }

    private boolean authenticate(String[] decodedCredentials) throws AuthenticationException{
        if ( decodedCredentials == null || decodedCredentials.length != 2 ) {
            throw new BasicAuthenticationException();
        }

        String username = decodedCredentials[0];
        String password = decodedCredentials[1];

        if ( VALID_USERNAME.equals( username ) && VALID_PASSWORD.equalsIgnoreCase( password ) ) {
            return true;
        }
        return false;
    }

}
