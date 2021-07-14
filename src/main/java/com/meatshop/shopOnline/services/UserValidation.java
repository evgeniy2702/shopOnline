package com.meatshop.shopOnline.services;

import com.meatshop.shopOnline.models.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Date;


@Service
public class UserValidation implements Validator {

    private final UserService userService;

    public UserValidation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        User user = (User) obj;
        ValidationUtils.rejectIfEmpty(errors,"born","bar_code.required");
        long dateLimit = new Date().getTime() - 140160L;
        if (userService.getUserByUsername(user.getUsername()).getBorn().after(new Date(dateLimit))) {
            errors.rejectValue("bar_code", "bar_code.required");
        }
    }
}
