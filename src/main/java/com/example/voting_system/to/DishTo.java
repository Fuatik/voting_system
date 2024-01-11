package com.example.voting_system.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {
    Long price;

    public DishTo(Integer id, String name, Long price) {
        super(id, name);
        this.price = price;
    }

    @Override
    public String toString() {
        return "DishTo: {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
