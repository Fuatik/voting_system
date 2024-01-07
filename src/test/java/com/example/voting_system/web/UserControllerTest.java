package com.example.voting_system.web;

import com.example.voting_system.UserTestUtil;
import com.example.voting_system.model.User;
import com.example.voting_system.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.voting_system.UserTestUtil.*;
import static com.example.voting_system.util.JsonUtil.writeValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractControllerTest {
    static final String URL = "/api/users/";

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + USER_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "search/by-email?email=" + ADMIN_EMAIL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + USER_ID))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(userRepository.findById(USER_ID).isPresent());
        Assertions.assertTrue(userRepository.findById(ADMIN_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void create() throws Exception {
        User newUser = UserTestUtil.getNew();
        perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newUser)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        User updated = UserTestUtil.getUpdated();
        perform(MockMvcRequestBuilders.put(URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isNoContent());
    }
}
