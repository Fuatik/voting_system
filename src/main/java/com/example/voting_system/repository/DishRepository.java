package com.example.voting_system.repository;

import com.example.voting_system.model.restaurant.Dish;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
}
