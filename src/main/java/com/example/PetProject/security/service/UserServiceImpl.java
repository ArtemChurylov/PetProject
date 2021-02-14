package com.example.PetProject.security.service;

import com.example.PetProject.security.models.CrmUser;
import com.example.PetProject.security.models.Role;
import com.example.PetProject.security.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(CrmUser crmUser) {
        User user = new User();
        user.setEmail(crmUser.getEmail());
        user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
        user.setName(crmUser.getName());
        user.setLastName(crmUser.getLastName());
        user.setRole(Role.USER);
        user.setBalance(100.0);
        userRepository.save(user);
    }

    @Override
    public void updateUser(CrmUser crmUser) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setEmail(crmUser.getEmail());
        user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
        user.setName(crmUser.getName());
        user.setLastName(crmUser.getLastName());
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findAll().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email).orElseThrow();
    }
}
