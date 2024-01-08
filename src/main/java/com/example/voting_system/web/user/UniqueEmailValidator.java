package com.example.voting_system.web.user;

import com.example.voting_system.model.HasIdAndEmail;
import com.example.voting_system.repository.UserRepository;
import com.example.voting_system.web.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UniqueEmailValidator implements Validator {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";

    private final UserRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            repository.findByEmailIgnoreCase(user.getEmail())
                    .ifPresent(dbUser -> {
                        if (request.getMethod().equals("PUT")) { // UPDATE
                            int dbId = dbUser.id();

                            // it is ok, if update ourself
                            if (user.getId() != null && dbId == user.id()) return;

                            // Workaround for update with user.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId) || (dbId == SecurityUtil.authId() && requestURI.contains("/profile")))
                                return;
                        }
                        errors.rejectValue("email", "", EXCEPTION_DUPLICATE_EMAIL);
                    });
        }
    }
}
