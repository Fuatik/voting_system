package com.example.voting_system.repository;

import com.example.voting_system.model.restaurant.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    Optional<Menu> findByRestaurantIdAndMenuDate(int restaurantId, LocalDate menuDate);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId ORDER BY m.menuDate DESC")
    List<Menu> findAllByRestaurantId(int restaurantId);
}
