package com.example.voting_system.web.restaurant;

import com.example.voting_system.model.restaurant.Menu;
import com.example.voting_system.to.MenuTo;
import com.example.voting_system.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.example.voting_system.repository.MenuRepository;
import com.example.voting_system.web.AbstractControllerTest;

import static com.example.voting_system.web.restaurant.AdminMenuController.REST_URL_MENU;
import static com.example.voting_system.web.restaurant.RestaurantTestData.*;
import static com.example.voting_system.web.user.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminMenuControllerTest extends AbstractControllerTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllMenus() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU, RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(menuTo1, menuTo3));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getMenuByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU + "/{menuDate}", RESTAURANT_ID, NOW.toLocalDate()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(menuTo1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_MENU + "/{menuDate}", RESTAURANT_ID, NOW.toLocalDate()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.findByRestaurantIdAndMenuDate(RESTAURANT_ID, NOW.toLocalDate()).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Menu newMenu = RestaurantTestData.getNewMenu();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_MENU, RESTAURANT_ID + 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andExpect(status().isCreated());

        Menu created = MENU_MATCHER.readFromJson(action);

        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.getExisted(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateMenu() throws Exception {
        MenuTo updatedMenuTo = RestaurantTestData.getUpdatedMenu();
        perform(MockMvcRequestBuilders.put(REST_URL_MENU + "?menuDate={menuDate}", RestaurantTestData.RESTAURANT_ID, RestaurantTestData.NOW.toLocalDate())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenuTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Menu updatedMenu = menuRepository.findByRestaurantIdAndMenuDate(RestaurantTestData.RESTAURANT_ID, RestaurantTestData.NOW.toLocalDate())
                .orElse(null);
        assertNotNull(updatedMenu);
        assertEquals(updatedMenuTo.getMenuDate(), updatedMenu.getMenuDate());
    }
}