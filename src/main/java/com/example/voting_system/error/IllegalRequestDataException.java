package com.example.voting_system.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class IllegalRequestDataException extends AppException{
    public IllegalRequestDataException(String message) {
        super(message);
    }
}
