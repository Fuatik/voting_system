package com.example.voting_system.web.restaurant;

import com.example.voting_system.model.restaurant.Menu;
import com.example.voting_system.model.restaurant.MenuItem;
import com.example.voting_system.model.restaurant.Restaurant;
import com.example.voting_system.to.MenuTo;
import com.example.voting_system.to.RestaurantTo;
import com.example.voting_system.web.MatcherFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class, "menuItems");
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "menuItems", "restaurant");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class, "menus");
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus", "votes");

    public static final int RESTAURANT_ID = 1;
    public static final int MENU_ID = 1;
    public static final LocalDateTime NOW = LocalDateTime.now();

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_ID, "Restaurant1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_ID + 1, "Restaurant2");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_ID + 2, "Restaurant3");
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT_ID + 3, "Restaurant4");

    public static final RestaurantTo restaurantTo1 = new RestaurantTo(RESTAURANT_ID, "Restaurant1", null);
    public static final RestaurantTo restaurantTo2 = new RestaurantTo(RESTAURANT_ID + 1, "Restaurant2", null);

    public static final MenuTo menuTo1 = new MenuTo(MENU_ID, NOW.toLocalDate(), Set.of(
            new MenuItem(null,"Dish1",  99L),
            new MenuItem(null,"Dish2", 999L),
            new MenuItem(null,"Dish3", 1099L)
    ));

    public static final MenuTo menuTo2 = new MenuTo(MENU_ID + 1, NOW.toLocalDate(), Set.of(
            new MenuItem(null,"Dish4", 399L),
            new MenuItem(null,"Dish5", 499L)
    ));

    public static final MenuTo menuTo3 = new MenuTo(MENU_ID + 2, LocalDate.of(2024, 1,8), Set.of(
            new MenuItem(null,"Dish6", 199L)
    ));

    public static Restaurant getNewRestaurant() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdatedRestaurant() {
        return new Restaurant(RESTAURANT_ID, "UpdatedName");
    }

    public static Menu getNewMenu() {
        return new Menu(null, NOW.toLocalDate(), Set.of(
                new MenuItem(null,"New Dish1", 1200L),
                new MenuItem(null,"New Dish2", 1500L)
        ), restaurant3);
    }

    public static MenuTo getUpdatedMenu() {
        return new MenuTo(MENU_ID, NOW.toLocalDate(), Set.of(
                new MenuItem(null,"Updated Dish1", 1000L),
                new MenuItem(null,"Updated Dish2", 1300L)
        ));
    }
}
