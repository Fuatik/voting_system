package com.example.voting_system.repository;

import com.example.voting_system.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Tag(name = "User Controller")
public interface UserRepository extends BaseRepository<User> {
    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email) ")
    Optional<User> findByEmailIgnoreCase(String email);
}
