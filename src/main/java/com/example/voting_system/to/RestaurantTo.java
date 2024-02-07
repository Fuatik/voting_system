package com.example.voting_system.to;

import com.example.voting_system.model.restaurant.Menu;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    List<Menu> menus;

    public RestaurantTo(Integer id, String name, List<Menu> menus) {
        super(id, name);
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "RestaurantTo: {" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
