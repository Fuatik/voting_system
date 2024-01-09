package com.example.voting_system.to;

public class DishTo extends NamedTo {
    private final Long price;

    public DishTo(Integer id, String name, Long price) {
        super(id, name);
        this.price = price;
    }

    @Override
    public String toString() {
        return "DishTo:" + id + '[' + name + ']' + '[' + price + ']';
    }
}
