package com.meatshop.shopOnline.services;


import com.meatshop.shopOnline.models.Role;
import com.meatshop.shopOnline.models.User;
import com.meatshop.shopOnline.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username){

        User user =userRepo.getUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Пользователь с логином " + username + " не удается найти");
        }

        return user;
    }

    public boolean findUserByUsernameException(String username){
        return userRepo.findAll().stream().map(User::getUsername).noneMatch(name-> name.equals(username));
    }

    public void saveUser(User user){
        User userDB = userRepo.getUserByUsername(user.getUsername());
        if(userDB == null){
            user.setRoles(Collections.singleton(new Role(2L,"ROLE_USER")));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(false);
            user.setBan_user(false);

            userRepo.save(user);
        } else {
            throw new EntityNotFoundException("Такого пользователя не существует");
        }
    }

    public void deleteUserById(Long id){
        User userDB = userRepo.getUserById(id);
        if(userDB != null){
            userRepo.deleteUserById(id);
        } else {
            throw  new EntityNotFoundException("Такого пользователя c id : " + id + " не существует");
        }
    }
    public void updateEnabledField(User userDB) {
        User exist = userRepo.getUserById(userDB.getId());
        if(exist != null){
            exist.setEnabled(userDB.getEnabled());
            userRepo.save(exist);
        }
    }

    public User getUserByUsername(String username) {
        try {
            return userRepo.getUserByUsername(username);
        } catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("Пользователя с таким Username: " + username + " не существует.") ;
        }
    }

    public User getUserById(Long id){
        return userRepo.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
