package com.greenorine.innovationsurfuapi.rest;

import com.greenorine.innovationsurfuapi.dto.AuthenticationRequestDto;
import com.greenorine.innovationsurfuapi.security.jwt.JwtTokenProvider;
import com.greenorine.innovationsurfuapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/public/")
public class PublicRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    public PublicRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("auth")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        var login = requestDto.getLogin();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, requestDto.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var user = userService.findByEmail(login);

        if (user == null)
            throw new UsernameNotFoundException("User with login " + login + "not found");
        var token = jwtTokenProvider.createToken(login, user.getRole());

        Map<Object, Object> response = new HashMap<>();

        response.put("username", login);
        response.put("id", user.getId());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}