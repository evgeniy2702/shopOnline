package com.meatshop.shopOnline.services;

import com.meatshop.shopOnline.models.ActivationCode;
import com.meatshop.shopOnline.models.User;
import com.meatshop.shopOnline.repositories.ActivationCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.util.UUID;

/**
 @author Zhurenko Evgeniy  08.06.21
 */

@Service
@Transactional
public class ActivationCodeService {

    private ActivationCodeRepo activationCodeRepo;
    private UserService userService;

    @Autowired
    public void setActivationCodeRepo(ActivationCodeRepo activationCodeRepo) {
        this.activationCodeRepo = activationCodeRepo;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Long saveActivationCodeUser(User user){
        ActivationCode activationCode = new ActivationCode();
        activationCode.setCode(UUID.randomUUID().toString());
        activationCode.setUsername(user.getUsername());
        activationCode.setPassword(user.getPassword());
        activationCode.setEmail(user.getEmail());
        activationCode.setPhone(user.getPhone());
        activationCodeRepo.save(activationCode);
        return activationCode.getId();
    }

    public ActivationCode getActivationCodeUser(Long idCode){
        return activationCodeRepo.getActivationCodeById(idCode);
    }

    public Long activateUser(String code){
        ActivationCode activationCodeDB = activationCodeRepo.getActivationCodeByCode(code);
        if(activationCodeDB != null){
            User user = new User();
            user.setUsername(activationCodeDB.getUsername());
            user.setPassword(activationCodeDB.getPassword());
            user.setEmail(activationCodeDB.getEmail());
            user.setPhone(activationCodeDB.getPhone());
            userService.saveUser(user);
            Long id = userService.getUserByUsername(user.getUsername()).getId();
            activationCodeRepo.deleteActivationCodeById(activationCodeDB.getId());
            return id;
        } else {
            return 0L;
        }
    }
}
