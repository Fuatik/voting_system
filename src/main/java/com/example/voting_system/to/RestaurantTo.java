package com.example.voting_system.to;

import com.example.voting_system.model.restaurant.Dish;
import lombok.EqualsAndHashCode;
import lombok.Setter;


import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    List<Dish> menu;
    @Setter
    Integer rating;

    public RestaurantTo(Integer id, String name, List<Dish> menu, Integer rating) {
        super(id, name);
        this.menu = menu;
        this.rating = rating;
    }



    @Override
    public String toString() {
        return "RestaurantTo: {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu='" + menu + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
