package com.meatshop.shopOnline.services;

import com.meatshop.shopOnline.models.Mail;
import com.meatshop.shopOnline.repositories.MailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Service
public class MailService {

    private MailRepo mailRepo;

    @Autowired
    public void setMailRepo(MailRepo mailRepo) {
        this.mailRepo = mailRepo;
    }

    public void saveMail(Mail mail){
        mailRepo.save(mail);
    }

    public void updateMail(Mail mail){
        mailRepo.save(mail);
    }


   public void deleteMail(Mail mail){
        mailRepo.delete(mail);
   }
}
