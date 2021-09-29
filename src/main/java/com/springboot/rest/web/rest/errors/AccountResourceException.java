package com.springboot.rest.web.rest.errors;
public class AccountResourceException extends RuntimeException {

        public AccountResourceException(String message) {
            super(message);
        }
    }