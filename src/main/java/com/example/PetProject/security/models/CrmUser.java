package com.example.PetProject.security.models;

import com.example.PetProject.security.validation.ConfirmPassword;
import com.example.PetProject.security.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ConfirmPassword
@UniqueEmail
public class CrmUser {

    @NotEmpty(message = "Can`t be empty.")
    @Email
    private String email;

    @NotEmpty(message = "Can`t be empty.")
    @Size(min = 6, message = "The min count of characters is 6.")
    private String password;

    @NotEmpty(message = "Can`t be empty.")
    @Size(min = 6, message = "The min count of characters is 6.")
    private String confirmPassword;

    @NotEmpty(message = "Can`t be empty.")
    @Size(min = 3, message = "The min count of characters is 3.")
    private String name;

    @NotEmpty(message = "Can`t be empty.")
    @Size(min = 3, message = "The min count of characters is 3.")
    private String lastName;

    public CrmUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
