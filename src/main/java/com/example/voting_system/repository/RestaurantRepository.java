package com.example.voting_system.repository;

import com.example.voting_system.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menu m LEFT JOIN FETCH r.votes v WHERE r.id=:id AND m.dishDate=:date AND v.voteDate=:date")
    Optional<Restaurant> getRestaurantWithMenuAndVotesById(Integer id, LocalDate date);

    @Query("SELECT r FROM Restaurant r " +
            "LEFT JOIN FETCH r.menu m " +
            "WHERE m.dishDate=:date " +
            "ORDER BY r.name ASC")
    Optional<List<Restaurant>> findAllWithMenus(LocalDate date);
}
