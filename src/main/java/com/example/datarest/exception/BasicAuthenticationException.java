package com.example.datarest.exception;

public class BasicAuthenticationException extends AuthenticationException {

    private static final String FAILED_BASIC_AUTHENTICATION = "401 Unauthorized - Basic Authentication required";

    public BasicAuthenticationException() {
        super( FAILED_BASIC_AUTHENTICATION );
    }

    public BasicAuthenticationException(Throwable throwable) {
        super(FAILED_BASIC_AUTHENTICATION, throwable);
    }
}
