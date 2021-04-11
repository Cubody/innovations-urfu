package com.greenorine.innovationsurfuapi.repository;

import com.greenorine.innovationsurfuapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByFullName (String name);

    User findByEmail (String email);

}
