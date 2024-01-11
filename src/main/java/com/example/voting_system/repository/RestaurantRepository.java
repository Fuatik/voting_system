package com.example.voting_system.repository;

import com.example.voting_system.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r " +
            "LEFT JOIN FETCH r.menu " +
            "LEFT JOIN FETCH r.votes " +
            "ORDER BY r.name ASC ")
    Optional<List<Restaurant>> findAllRestaurantsWithMenuAndVotes();

    @Query("SELECT r FROM Restaurant r " +
            "LEFT JOIN FETCH r.menu " +
            "LEFT JOIN FETCH r.votes " +
            "WHERE r.id=:id")
    Optional<Restaurant> getRestaurantWithMenuAndVotesById(int id);

    @Query("SELECT r FROM Restaurant r " +
            "LEFT JOIN FETCH r.menu m " +
            "WHERE m.dishDate=:date " +
            "ORDER BY r.name ASC")
    Optional<List<Restaurant>> findAllWithMenusByDate(LocalDate date);

    @Override
    @EntityGraph(attributePaths = {"menu"})
    default Restaurant getExisted(int id) {
        return BaseRepository.super.getExisted(id);
    }
}
