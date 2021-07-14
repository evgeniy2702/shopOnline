package com.meatshop.shopOnline.controllers;

import com.meatshop.shopOnline.models.User;
import com.meatshop.shopOnline.services.ActivationCodeService;
import com.meatshop.shopOnline.services.MailSenderService;
import com.meatshop.shopOnline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Controller
public class RegistrationLogin {

    private UserService userService;
    private MailSenderService mailSenderService;
    private ActivationCodeService activationCodeService;

    @Value("${upload.mail.link}")
    private String link;

    @Value("${upload.domenname}")
    private String domenname;

    @Autowired
    public RegistrationLogin(UserService userService,
                             MailSenderService mailSenderService,
                             ActivationCodeService activationCodeService) {
        this.userService = userService;
        this.mailSenderService = mailSenderService;
        this.activationCodeService = activationCodeService;
    }

    @GetMapping(value = {"/login_page"})
    public String loginGet(Model model,
                           @RequestParam(name = "style",required = false) boolean style){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HeaderController headerController = new HeaderController();
        headerController.defineGuestUserAdmin(model, auth);
        if(auth == null){
            return "redirect:/guest/about";
        } else {
            if (!auth.getPrincipal().toString().equalsIgnoreCase("anonymousUser")) {
                if (auth.getAuthorities().stream().anyMatch(role -> role.toString()
                        .equalsIgnoreCase("ROLE_USER"))) {
                    User userDB = userService.getUserByUsername(auth.getName());
                    userDB.setEnabled(true);
                    userService.updateEnabledField(userDB);
                    model.addAttribute("user_person", userDB);
                    return "redirect:/user/about/" + userDB.getId();
                } else {
                    model.addAttribute("admin_person", userService.getUserByUsername("admin"));
                    return "redirect:/admin/about/1";
                }
            }
        }
        return "login_page";
    }

    @PostMapping("/authentication")
    public String LoginPost(@RequestParam(value = "error", required = false) Boolean error,
                            @RequestParam(value = "message", required = false) String message,
                            @RequestParam(value = "username", required = false) String username,
                            Model model ){
        if(Boolean.TRUE.equals(error)){
            model.addAttribute("error", true);
            model.addAttribute("access", true);
            model.addAttribute("message", message);
            model.addAttribute("username", username);
            model.addAttribute("guest", true);
        }
        return "login_page";
    }

    @PostMapping("/registration")
    public ModelAndView registration(@ModelAttribute User user, ModelAndView modelAndView) {
        User userDB = userService.getUserByUsername(user.getUsername());
        if (userDB == null) {
           Long idCode = activationCodeService.saveActivationCodeUser(user);
           if(idCode > 0L){
                String message = String.format(
                        "Hello, %s! \n" +
                                "Добро пожаловать на ресурс " + domenname + ".\n Для завершения регистрации перейдите по следующей ссылке : " +
                                link + "%s",
                        user.getUsername(),
                        activationCodeService.getActivationCodeUser(idCode).getCode());

                mailSenderService.send(activationCodeService.getActivationCodeUser(idCode).getEmail(), "Registration on " + domenname, message);

                modelAndView.addObject("message", "Для продолжения регистрации перейдите в почтовый ящик, указанный при регистрации "
                        + user.getEmail());
               modelAndView.addObject("access", true);
               modelAndView.addObject("guest", true);
               modelAndView.setViewName("status/email-message");
                return modelAndView;
           } else {
               sendLoginPageData(modelAndView);
               return modelAndView;
           }
        } else {
            sendLoginPageData(modelAndView);
            return modelAndView;
        }
    }

        // Ending registration process when receive email and follow by email link
        @GetMapping("/activate/{code}")
        public ModelAndView activate(ModelAndView modelAndView,
                @PathVariable(name = "code", required = false) String code){

            Long idActivateUser = activationCodeService.activateUser(code);

            if(idActivateUser != 0L){
                modelAndView.addObject("error", true);
                modelAndView.addObject("message", "Пользователь активирован успешно");
                modelAndView.addObject("user", true);
                modelAndView.addObject("idUser",idActivateUser );
                modelAndView.setViewName("redirect:/login_process");
            } else {
                modelAndView.addObject("message", "Код активации не найден");
                modelAndView.addObject("guest", true);
                modelAndView.setViewName("status/email-message");
            }
            return modelAndView;
        }


        private void sendLoginPageData(ModelAndView modelAndView){
            modelAndView.addObject("bool", true);
            modelAndView.addObject("access", true);
            modelAndView.addObject("style", "display:none;");
            modelAndView.addObject("unStyle", "display:block;");
            modelAndView.addObject("errorRegistration", "Такой профиль уже существует");
            modelAndView.addObject("guest", true);
            modelAndView.setViewName("login_page");
        }
 }
