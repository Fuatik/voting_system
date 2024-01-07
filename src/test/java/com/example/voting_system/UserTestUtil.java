package com.example.voting_system;

import com.example.voting_system.model.Role;
import com.example.voting_system.model.User;

import java.util.List;

public class UserTestUtil {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final String USER_EMAIL = "user@gmail.com";
    public static final String ADMIN_EMAIL = "admin@gmail.com";

    public static User getNew() {
        return new User(null, "new@gmail.com", "New_First", "New_Last", "newpass", List.of(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "update@gmail.com", "update_First", "update_Last", "update_pass", List.of(Role.USER));
    }
}
