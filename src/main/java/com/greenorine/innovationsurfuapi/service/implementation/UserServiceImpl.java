package com.greenorine.innovationsurfuapi.service.implementation;

import com.greenorine.innovationsurfuapi.model.User;
import com.greenorine.innovationsurfuapi.repository.UserRepository;
import com.greenorine.innovationsurfuapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);

        log.info("IN save: user {} successfully saved", user);
    }

    @Override
    public List<User> getAll() {
        var users = userRepository.findAll();
        log.info("IN getAll: found {} users", users.size());
        return users;
    }

    @Override
    public User findByFullName(String fullName) {
        var user = userRepository.findByFullName(fullName);

        if (user == null) {
            log.info("IN findByFullName: no user found by fullName {}", fullName);
            return null;
        }

        log.info("IN findByFullName: user {} found by fullName {}", user, fullName);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        var user = userRepository.findByEmail(email);

        if (user == null) {
            log.info("IN findByEmail: no user {} found by email {}", user, email);
            return null;
        }

        log.info("IN findByEmail: user {} found by email {}", user, email);

        return user;
    }

    @Override
    public User findById(Long id) {
        var user = userRepository.findById(id).orElse(null);

        if (user == null) {
            log.info("IN findByFullName: no user {} found by id {}", user, id);
            return null;
        }

        log.info("IN findByFullName: user {} found by id {}", user, id);
        return user;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete: user with id {} deleted", id);
    }
}
