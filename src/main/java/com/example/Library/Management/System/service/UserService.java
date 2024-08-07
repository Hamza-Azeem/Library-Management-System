package com.example.Library.Management.System.service;

import com.example.Library.Management.System.entity.Patron;
import com.example.Library.Management.System.entity.User;
import com.example.Library.Management.System.models.RegistrationRequest;

public interface UserService{
    public User findUserByUsername(String username);
    public void saveUser(RegistrationRequest registrationRequest);
    public void addPatronToUser(User user, Patron patron);
}
