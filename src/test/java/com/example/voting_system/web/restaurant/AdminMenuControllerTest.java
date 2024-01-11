package com.example.voting_system.web.restaurant;

import com.example.voting_system.model.restaurant.Dish;
import com.example.voting_system.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.example.voting_system.repository.DishRepository;
import com.example.voting_system.web.AbstractControllerTest;

import static com.example.voting_system.web.restaurant.RestaurantTestData.*;
import static com.example.voting_system.web.user.UserTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminMenuControllerTest extends AbstractControllerTest {

    public static final String REST_URL_MENU = "/api/admin/restaurants/1/menu";
    private static final String REST_URL_MENU_SLASH = REST_URL_MENU + '/';

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(dishTo1, dishTo2, dishTo3));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getDishInMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU_SLASH + DISH_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(dishTo1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void removeDishFromMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_MENU_SLASH + DISH_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(DISH_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addDishToMenu() throws Exception {
        Dish newDish = getNewDish();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_MENU)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated());

        Dish created = DISH_MATCHER.readFromJson(action);

        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.getExisted(newId), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDishInMenu() throws Exception {
        Dish updated = getUpdatedDish();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL_MENU_SLASH + DISH_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(repository.getExisted(DISH_ID), getUpdatedDish());
    }
}