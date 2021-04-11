package com.greenorine.innovationsurfuapi.repository;

import com.greenorine.innovationsurfuapi.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Application findByTitle(String title);
}
