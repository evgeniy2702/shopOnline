package com.meatshop.shopOnline.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Controller
@RequestMapping("/guest/")
public class GuestController {

    @GetMapping("about")
    public String aboutPage(Model model){
        model.addAttribute("guest", true);
        return "about";
    }

    @GetMapping("events")
    public String eventsPage(Model model){
        model.addAttribute("guest", true);
        return "events";
    }

    @GetMapping("services")
    public String servicesPage(Model model){
        model.addAttribute("guest", true);
        return "services";
    }

    @GetMapping("index")
    public String indexPage(Model model){
        model.addAttribute("guest", true);
        return "index";
    }
}
