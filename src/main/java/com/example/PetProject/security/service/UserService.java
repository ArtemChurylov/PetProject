package com.example.PetProject.security.service;

import com.example.PetProject.security.models.CrmUser;
import com.example.PetProject.security.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    void save(CrmUser crmUser);

    Optional<User> findByEmail(String email);
}
