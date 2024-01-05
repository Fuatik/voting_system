package com.example.voting_system;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class VotingSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(VotingSystemApplication.class, args);
    }
}
