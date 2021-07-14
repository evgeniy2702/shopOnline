package com.meatshop.shopOnline.controllersException;

import com.meatshop.shopOnline.models.ApiError;
import com.meatshop.shopOnline.services.UserService;

import javassist.NotFoundException;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 @author Zhurenko Evgeniy 8.06.21
 */


@Controller
public class MyErrorController implements ErrorController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/error")
    public String handleError( HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute("javax.servlet.error.message");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getGuestUserAdminProp(authentication,
                              status,
                              message,
                              model);
        return "error";

    }

    private void getGuestUserAdminProp(Authentication authentication, Object status, Object message, Model model){
        Integer statusCode = Integer.valueOf(status.toString());
        String msg = message.toString();
        if(authentication.getPrincipal().toString().equalsIgnoreCase("anonymousUser")) {
            model.addAttribute("guest", true);
        }
        else if(authentication.getAuthorities().stream().anyMatch(role -> role.toString()
                .equalsIgnoreCase("ROLE_USER"))) {
            model.addAttribute("user", true);
            model.addAttribute("user_person", userService.getUserByUsername(authentication.getName()));
        } else if(authentication.getAuthorities().stream().anyMatch(role -> role.toString()
                .equalsIgnoreCase("ROLE_ADMIN"))){
            model.addAttribute("admin", true);
        }
        if(statusCode == 500) {
            msg = "INTERNAL_SERVER_ERROR";
            model.addAttribute("account", "Ошибка " + " " + statusCode + " " + msg);
        } else if(statusCode == 404){
            msg = "NOT FOUND";
            model.addAttribute("account", "Ошибка " + " " + statusCode + " " + msg);
        } else if(statusCode == 403){
            msg = "FORBIDDEN";
            model.addAttribute("account", "Ошибка " + " " + statusCode + " " + msg);
        } else {
            model.addAttribute("account", "Ошибка " + " " + statusCode + " " + msg);
        }
    }
}













