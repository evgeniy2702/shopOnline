package com.meatshop.shopOnline.repositories;

import com.meatshop.shopOnline.models.ActivationCode;
import com.meatshop.shopOnline.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 @author Zhurenko Evgeniy  08.06.21
 */


public interface ActivationCodeRepo extends JpaRepository<ActivationCode, Long> {

    ActivationCode getActivationCodeById(Long id);

    void deleteActivationCodeById(Long id);

    ActivationCode getActivationCodeByCode(String code);
}
