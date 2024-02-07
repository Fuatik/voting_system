package com.example.voting_system.repository;

import com.example.voting_system.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r " +
            "LEFT JOIN FETCH r.menus m " +
            "WHERE m.menuDate=:date ")
    List<Restaurant> findAllWithMenusByDate(LocalDate date);

    @Override
    @EntityGraph(attributePaths = {"menus"})
    default Restaurant getExisted(int id) {
        return BaseRepository.super.getExisted(id);
    }
}
