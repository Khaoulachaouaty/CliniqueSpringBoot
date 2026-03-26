package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.User;
import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findByUsername(String username);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
}