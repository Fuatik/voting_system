package com.example.voting_system.error;

import lombok.NonNull;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message);
    }
}
