package com.meatshop.shopOnline.repositories;

import com.meatshop.shopOnline.models.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 @author Zhurenko Evgeniy  08.06.21
 */

public interface MailRepo extends JpaRepository<Mail, Long> {

    Mail getMailById(Long id);
}
