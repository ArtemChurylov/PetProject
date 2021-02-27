package com.example.PetProject.security.service;

import com.example.PetProject.security.models.CrmUser;
import com.example.PetProject.security.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

// User service interface extends UserDetailsService which is necessary for Spring Security

public interface UserService extends UserDetailsService {

    void save(CrmUser crmUser);
    void updateUser(CrmUser crmUser);

    Optional<User> findByEmail(String email);
}
