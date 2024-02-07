package com.example.voting_system.to;

import com.example.voting_system.model.restaurant.MenuItem;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;
import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    LocalDate menuDate;
    Set<MenuItem> menuItems;

    public MenuTo(Integer id, LocalDate menuDate, Set<MenuItem> menuItems) {
        super(id);
        this.menuDate = menuDate;
        this.menuItems = menuItems;
    }
}
