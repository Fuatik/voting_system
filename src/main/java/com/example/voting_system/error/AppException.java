package com.example.voting_system.error;

import lombok.NonNull;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {

    public AppException(@NonNull String message) {
        super(message);
    }
}
