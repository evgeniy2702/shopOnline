package com.meatshop.shopOnline.controllers;

import com.meatshop.shopOnline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Controller
@RequestMapping("/admin/")
public class AdminController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("about/{id}")
    public String userAbout(@PathVariable(name = "id", required = false) Long idAdmin,
                            Model model){
        model.addAttribute("admin", true);
        model.addAttribute("admin_person", userService.getUserById(idAdmin));
        return "about";
    }

    @GetMapping("events/{id}")
    public String userEvents (@PathVariable(name = "id") Long idAdmin,
                              Model model){
        model.addAttribute("user_person", userService.getUserById(idAdmin));
        model.addAttribute("user", true);
        return "events";
    }

    @GetMapping("services/{id}")
    public String userServices (@PathVariable(name = "id") Long idAdmin,
                                Model model){
        model.addAttribute("user_person", userService.getUserById(idAdmin));
        model.addAttribute("user", true);
        return "services";
    }

    @GetMapping("index/{id}")
    public String userIndex (@PathVariable(name = "id") Long idAdmin,
                             Model model){
        model.addAttribute("user_person", userService.getUserById(idAdmin));
        model.addAttribute("user", true);
        return "index";
    }
}
