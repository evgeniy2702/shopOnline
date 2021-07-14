package com.meatshop.shopOnline.security;

import com.meatshop.shopOnline.models.User;
import com.meatshop.shopOnline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.ForwardLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;


/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Service
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler  {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {

        User userDB = userService.getUserByUsername(authentication.getName());

        if(userDB != null){
            userDB.setEnabled(false);
            userService.updateEnabledField(userDB);
        }

        httpServletResponse.sendRedirect("/guest/about");
    }
}
