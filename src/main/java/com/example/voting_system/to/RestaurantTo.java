package com.example.voting_system.to;

import com.example.voting_system.model.restaurant.Dish;
import com.example.voting_system.model.restaurant.Vote;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.util.List;
import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    List<Dish> menu;

    Set<Vote> votes;

    public RestaurantTo(Integer id, String name, List<Dish> menu, Set<Vote> votes) {
        super(id, name);
        this.menu = menu;
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "RestaurantTo: {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu='" + menu + '\'' +
                ", votes='" + votes + '\'' +
                '}';
    }
}
