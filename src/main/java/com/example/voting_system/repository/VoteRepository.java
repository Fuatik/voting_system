package com.example.voting_system.repository;

import com.example.voting_system.model.restaurant.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.voteTime BETWEEN :start AND :end")
    Optional<Vote> findByUserIdAndVoteTimeBetween(int userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END " +
            "FROM Vote v WHERE v.user.id=:userId AND v.voteTime=:votTime AND v.restaurant.id=:restaurantId")
    boolean existsByUserIdAndVoteTimeAndRestaurantId(int userId, LocalDateTime voteTime, int restaurantId);

    @Query("SELECT v FROM Vote v WHERE v.voteTime >= :startDate AND v.voteTime < :endDate")
    List<Vote> findVotesForToday(LocalDateTime startDate, LocalDateTime endDate);
}
