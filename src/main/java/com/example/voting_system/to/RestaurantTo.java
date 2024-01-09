package com.example.voting_system.to;

public class RestaurantTo extends NamedTo {
    public RestaurantTo(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "RestaurantTo:" + id + '[' + name + ']';
    }
}
