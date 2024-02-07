package com.example.voting_system.repository;

import com.example.voting_system.model.restaurant.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.voteDate=:date")
    Optional<Vote> findByUserIdAndVoteDate(int userId, LocalDate date);

    List<Vote> findAllByUserId(int userId);
}
