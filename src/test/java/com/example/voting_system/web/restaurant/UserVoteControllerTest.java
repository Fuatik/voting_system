package com.example.voting_system.web.restaurant;

import com.example.voting_system.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.*;

import static com.example.voting_system.web.restaurant.RestaurantTestData.*;
import static com.example.voting_system.web.restaurant.UserVoteController.REST_URL;
import static com.example.voting_system.web.user.UserTestData.GUEST_MAIL;
import static com.example.voting_system.web.user.UserTestData.USER_MAIL;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserVoteControllerTest extends AbstractControllerTest {

    @Autowired
    private UserVoteController voteController;

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void vote() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + "/{id}", RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.get(REST_URL + "/votes/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurantId").value(RESTAURANT_ID));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void reVote() throws Exception {
        voteController.setEndVotingTime(LocalTime.MAX);

        perform(MockMvcRequestBuilders.patch(REST_URL + "/{id}", RESTAURANT_ID + 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.get(REST_URL + "/votes/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurantId").value(RESTAURANT_ID + 1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void reVoteAfterDeadline() throws Exception {

        voteController.setEndVotingTime(LocalTime.MIN);

        perform(MockMvcRequestBuilders.patch(REST_URL + "/{id}", RESTAURANT_ID + 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantTo1, restaurantTo2));
    }
}