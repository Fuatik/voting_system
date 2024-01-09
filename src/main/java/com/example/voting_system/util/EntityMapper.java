package com.example.voting_system.util;

import com.example.voting_system.model.user.Role;
import com.example.voting_system.model.user.User;

import com.example.voting_system.to.UserTo;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;

@UtilityClass
public class EntityMapper {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

/*    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail());
        user.setPassword(userTo.getPassword());
        return user;
    }*/

    public static <T, U> T updateFromTo(T entity, U entityTo) {
        BeanUtils.copyProperties(entityTo, entity);
        return entity;
    }

    public static <T, U> U updateToTo(T entity, U entityTo) {
        BeanUtils.copyProperties(entity, entityTo);
        return entityTo;
    }
}
