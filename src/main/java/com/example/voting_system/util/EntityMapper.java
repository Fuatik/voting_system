package com.example.voting_system.util;

import com.example.voting_system.model.restaurant.Restaurant;
import com.example.voting_system.model.user.Role;
import com.example.voting_system.model.user.User;

import com.example.voting_system.to.RestaurantTo;
import com.example.voting_system.to.UserTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EntityMapper {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static RestaurantTo getTo(Restaurant restaurant) {
        return new RestaurantTo(
                restaurant.id(),
                restaurant.getName(),
                restaurant.getMenu(),
                restaurant.getVotes());
    }
}
