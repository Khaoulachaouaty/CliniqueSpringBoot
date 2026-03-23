package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.User;

public interface UserService {
    User saveUser(User user);
    User updateUser(User user);
    void deleteUser(User user);
    void deleteUserById(Long id);
    Optional<User> getUser(Long id);
    List<User> getAllUsers();
    Optional<User> findByUsername(String username);
}