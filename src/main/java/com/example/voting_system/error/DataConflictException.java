package com.example.voting_system.error;

import lombok.NonNull;

public class DataConflictException extends AppException {
    public DataConflictException(String message) {
        super(message);
    }
}
