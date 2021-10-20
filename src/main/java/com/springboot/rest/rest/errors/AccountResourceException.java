package com.springboot.rest.rest.errors;
public class AccountResourceException extends RuntimeException {

        public AccountResourceException(String message) {
            super(message);
        }
    }