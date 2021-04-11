package com.greenorine.innovationsurfuapi.service;

import com.greenorine.innovationsurfuapi.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    List<User> getAll();

    User findByFullName(String fullName);

    User findByEmail(String email);

    User findById(Long id);

    void delete(Long id);
}