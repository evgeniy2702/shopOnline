package com.meatshop.shopOnline.controllers;

import com.meatshop.shopOnline.models.User;
import com.meatshop.shopOnline.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Controller
public class AccessDeniedController {

    private final UserRepo userRepo;

    @Autowired
    public AccessDeniedController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // processing a request in case of access denied
    @GetMapping("/accessDenied")
    public String accessDenied(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null){
            model.addAttribute("guest", true);
        }else {
            User userDB = userRepo.getUserByUsername(auth.getName());
            if (userDB.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_USER"))) {
                model.addAttribute("user", true);
                model.addAttribute("user_person",userDB);
                model.addAttribute("account", "Аккаунт " + userDB.getFirst_name() + " " + userDB.getLast_name());
            } else if(userDB.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
                model.addAttribute("admin", true);
                model.addAttribute("account", "Аккаунт " + userDB.getFirst_name());
                model.addAttribute("admin_person", userRepo.getUserById(1L));
            }
        }

        return "status/access_denied";
    }
}
