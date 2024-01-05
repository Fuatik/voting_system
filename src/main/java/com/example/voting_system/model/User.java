package com.example.voting_system.model;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<Role> roles;

}
