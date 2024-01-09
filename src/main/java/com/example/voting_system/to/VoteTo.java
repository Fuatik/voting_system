package com.example.voting_system.to;

import com.example.voting_system.model.user.User;

public class VoteTo extends BaseTo {
    private final Integer userId;
    private final Integer restaurantId;

    public VoteTo(Integer id, Integer userId, Integer restaurantId) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "VoteTo:" + id + " userId:" + userId + " restaurantId:" + restaurantId;
    }
}
