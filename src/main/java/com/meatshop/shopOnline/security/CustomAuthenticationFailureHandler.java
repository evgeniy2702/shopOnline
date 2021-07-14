package com.meatshop.shopOnline.security;

import com.meatshop.shopOnline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private UserService userRepo;

    @Autowired
    public void setUserRepo(UserService userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException, AuthenticationException {
        String message = "";
        String username = request.getParameter("username");

        if(userRepo.findUserByUsernameException(username)){
            message = "Пользователя с таким логином не найдено";
        } else if(exception.getClass() == BadCredentialsException.class) {
            message = "Проверьте свой пароль";
        }

        request.getRequestDispatcher(String.format("/authentication?error=true&message=%s&username=%s", message,username))
                .forward(request, response);
    }
}
