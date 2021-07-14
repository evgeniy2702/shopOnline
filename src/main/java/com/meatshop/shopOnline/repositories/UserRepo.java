package com.meatshop.shopOnline.repositories;

import com.meatshop.shopOnline.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 @author Zhurenko Evgeniy  08.06.21
 */

public interface UserRepo extends JpaRepository<User, Long> {

    User getUserByUsername(String username);

    User getUserById(Long id);

    Optional<User> findById(Long id);

    void deleteUserById(Long id);
}
