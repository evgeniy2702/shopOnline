package com.meatshop.shopOnline.controllers;


import com.meatshop.shopOnline.models.Mail;
import com.meatshop.shopOnline.services.MailSenderService;
import com.meatshop.shopOnline.services.MailService;
import com.meatshop.shopOnline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Controller
@RequestMapping("/")
public class MailController {

    private MailSenderService mailSenderService;
    private MailService mailService;
    private UserService userService;

    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    public void setMailSenderService(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = {"guest/send_mail","user/send_mail/{id}","admin/send_mail/{id}"})
    public String sendMail(@ModelAttribute Mail mail,
                           @PathVariable(name = "id", required = false) Long id,
                           Model model){
        mailSenderService.sendMailMessage(mail.getEmail(), mail.getSubject(), mail.getMessage(), mail.getName(), mail.getTelephone());
        mailService.saveMail(mail);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HeaderController headerController = new HeaderController();
        headerController.defineGuestUserAdmin(model,auth);
        if(id != null)
            addUserOrAdminForPage(model, id);
        model.addAttribute("mail", false);
        model.addAttribute("access", true);
        model.addAttribute("email", email);
        model.addAttribute("mail_message", "Ваше письмо отправлено. Спасибо за обращение.");
        return "mail";
    }

    private void addUserOrAdminForPage(Model model, Long id){
        model.addAttribute("user_person", userService.getUserById(id));
    }
}
