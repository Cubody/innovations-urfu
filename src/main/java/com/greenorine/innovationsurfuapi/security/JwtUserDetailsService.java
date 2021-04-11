package com.greenorine.innovationsurfuapi.security;

import com.greenorine.innovationsurfuapi.security.jwt.JwtUserFactory;
import com.greenorine.innovationsurfuapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userService.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User with fullName" + email + "not found");
        }

        var jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByUsername: user with username {} successfully loaded", email);
        return jwtUser;
    }
}
