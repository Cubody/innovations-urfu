package com.greenorine.innovationsurfuapi.service;

import com.greenorine.innovationsurfuapi.model.Application;

import java.util.List;

public interface ApplicationService {
    void save(Application user);

    List<Application> getAll();

    Application findById(Long id);

    void delete(Long id);
}
