package com.example.voting_system.web.restaurant;

import com.example.voting_system.model.restaurant.Dish;
import com.example.voting_system.model.restaurant.Restaurant;
import com.example.voting_system.to.DishTo;
import com.example.voting_system.web.MatcherFactory;

import java.time.LocalDate;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<DishTo> DISH_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(DishTo.class);
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "dishDate", "restaurant");
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menu", "votes");

    public static final int RESTAURANT_ID = 1;
    public static final int DISH_ID = 1;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_ID, "Restaurant1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_ID + 1, "Restaurant2");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_ID + 2, "Restaurant3");
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT_ID + 3, "Restaurant4");

    public static final DishTo dishTo1 = new DishTo(DISH_ID, "Dish1",  99L);
    public static final DishTo dishTo2 = new DishTo(DISH_ID + 1, "Dish2", 999L);
    public static final DishTo dishTo3 = new DishTo(DISH_ID + 2, "Dish3", 1099L);

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_ID, "UpdatedName");
    }

    public static Dish getNewDish() {
        return new Dish(null, "New Dish", LocalDate.now(), 1200L, restaurant1);
    }

    public static Dish getUpdatedDish() {
        return new Dish(DISH_ID, "UpdatedName", LocalDate.now(), 666L, restaurant1);
    }
}
