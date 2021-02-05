package com.example.PetProject.security.service;

import com.example.PetProject.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
