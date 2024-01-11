package com.example.voting_system.to;

import com.example.voting_system.model.restaurant.Dish;
import com.example.voting_system.validation.NoHtml;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    Set<Dish> menu;

    @NoHtml
    Integer rating;

    public RestaurantTo(Integer id, String name, Set<Dish> menu, Integer rating) {
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
