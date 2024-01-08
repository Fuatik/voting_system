package com.example.voting_system.web;

import com.example.voting_system.error.IllegalRequestDataException;
import com.example.voting_system.model.HasId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestValidation {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }
}
