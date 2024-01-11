package com.example.voting_system.web.restaurant;

import com.example.voting_system.model.restaurant.Vote;
import com.example.voting_system.repository.VoteRepository;
import com.example.voting_system.util.JsonUtil;
import com.example.voting_system.web.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.example.voting_system.web.restaurant.RestaurantTestData.*;
import static com.example.voting_system.web.restaurant.UserVoteController.REST_URL;
import static com.example.voting_system.web.user.UserTestData.GUEST_MAIL;
import static com.example.voting_system.web.user.UserTestData.USER_MAIL;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    public void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String currentTimePlusOneHour = LocalDateTime.now().plusHours(1).format(formatter);
        System.setProperty("voting.end-time", currentTimePlusOneHour);
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void vote() throws Exception {
        Vote vote = getNewVote();

        perform(MockMvcRequestBuilders.patch(REST_URL + "/" + RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andDo(print())
                .andExpect(status().isNoContent());

        int expectedRating = restaurantTo1.getRating() + 1;
        int actualRating = voteRepository.countByRestaurantIdAndVoteDate(RESTAURANT_ID, LocalDate.now()).orElse(0);

        Assertions.assertEquals(expectedRating, actualRating);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void reVote() throws Exception {
        Vote vote = getUpdatedVote();

        perform(MockMvcRequestBuilders.patch(REST_URL + "/" + RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(vote)))
                .andDo(print())
                .andExpect(status().isNoContent());

        int expectedRestaurant1Rating = restaurantTo1.getRating() + 1;
        int actualRestaurant1Rating = voteRepository.countByRestaurantIdAndVoteDate(RESTAURANT_ID, LocalDate.now()).orElse(0);

        Assertions.assertEquals(expectedRestaurant1Rating, actualRestaurant1Rating);

        int expectedRestaurant4Rating = restaurantTo4.getRating() - 1;
        int actualRestaurant4Rating = voteRepository.countByRestaurantIdAndVoteDate(RESTAURANT_ID + 3, LocalDate.now()).orElse(0);

        Assertions.assertEquals(expectedRestaurant4Rating, actualRestaurant4Rating);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantTo1, restaurantTo3, restaurantTo4));
    }
}