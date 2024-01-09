package com.example.voting_system.repository;

import com.example.voting_system.model.restaurant.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.voteTime BETWEEN :start AND :end")
    Optional<Vote> findByUserIdAndVoteTimeBetween(int userId, LocalDateTime start, LocalDateTime end);
}
