package com.example.voting_system;

import com.example.voting_system.model.Role;
import com.example.voting_system.model.User;
import com.example.voting_system.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestUtil {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final String USER_EMAIL = "user@gmail.com";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final User user = new User(USER_ID, USER_EMAIL, "User_First", "User_Last", "password", List.of(Role.USER));
    public static final User admin = new User(ADMIN_ID, ADMIN_EMAIL, "Admin_First", "Admin_Last", "admin", List.of(Role.ADMIN, Role.USER));

    public static User getNew() {
        return new User(null, "new@gmail.com", "New_First", "New_Last", "newpass", List.of(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "update@gmail.com", "update_First", "update_Last", "update_pass", List.of(Role.USER));
    }

    public static void assertEquals(User actual, User expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("password").isEqualTo(expected);
    }

    public static void assertNoIdEquals(User actual, User expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("id", "password").isEqualTo(expected);
    }

    public static User asUser(MvcResult mvcResult) throws IOException {
        String jsonActual = mvcResult.getResponse().getContentAsString();
        return JsonUtil.readValue(jsonActual, User.class);
    }

    public static ResultMatcher jsonMatcher(User expected, BiConsumer<User, User> equalsAssertion) {
        return mvcResult -> equalsAssertion.accept(asUser(mvcResult), expected);
    }
}
