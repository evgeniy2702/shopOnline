package com.meatshop.shopOnline.controllers;
import com.meatshop.shopOnline.models.ApiError;
import com.meatshop.shopOnline.services.UserService;
import com.sun.jdi.request.DuplicateRequestException;
import com.sun.jdi.request.ExceptionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Controller
@RequestMapping("/user/")
public class UserController{

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("about/{id}")
    public String userAbout (@PathVariable(name = "id") Long id,
                             Model model){
        model.addAttribute("user_person", userService.getUserById(id));
        model.addAttribute("user", true);
        return "about";
    }

    @GetMapping("events/{id}")
    public String userEvents (@PathVariable(name = "id") Long id,
                             Model model){
        model.addAttribute("user_person", userService.getUserById(id));
        model.addAttribute("user", true);
        return "events";
    }

    @GetMapping("services/{id}")
    public String userServices (@PathVariable(name = "id") Long id,
                             Model model){
        model.addAttribute("user_person", userService.getUserById(id));
        model.addAttribute("user", true);
        return "services";
    }

    @GetMapping("index/{id}")
    public String userIndex (@PathVariable(name = "id") Long id,
                                Model model){
        model.addAttribute("user_person", userService.getUserById(id));
        model.addAttribute("user", true);
        return "index";
    }
}
