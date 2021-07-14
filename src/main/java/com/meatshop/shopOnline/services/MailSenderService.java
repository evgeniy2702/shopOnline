package com.meatshop.shopOnline.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Service
public class MailSenderService {

    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    public void setJavaMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String email, String subject, String message){
        SimpleMailMessage mailMessage =new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setSentDate(new Date());

        mailSender.send(mailMessage);
    }

    public void sendMailMessage(String email, String subject, String message, String name, String telephone){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(email);
        mailMessage.setTo(username);
        mailMessage.setSubject(subject);
        mailMessage.setText(message + " " + "\nОт : " + name + "\nтел. : " + telephone + "\nemail : " + email);
        mailMessage.setSentDate(new Date());

        mailSender.send(mailMessage);
    }

}
