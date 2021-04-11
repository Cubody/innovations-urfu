package com.greenorine.innovationsurfuapi.rest;

import com.greenorine.innovationsurfuapi.dto.ApplicationDto;
import com.greenorine.innovationsurfuapi.model.ApplicationStatus;
import com.greenorine.innovationsurfuapi.model.Role;
import com.greenorine.innovationsurfuapi.security.jwt.JwtTokenProvider;
import com.greenorine.innovationsurfuapi.service.ApplicationService;
import com.greenorine.innovationsurfuapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "api/v1/user/")
public class UserRestControllerV1 {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserRestControllerV1(ApplicationService applicationService, UserService userService,
                                JwtTokenProvider jwtTokenProvider) {
        this.applicationService = applicationService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/getInfo")
    public ResponseEntity GetUserInfo(@RequestParam long id) {
        var user = userService.findById(id);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        var response = new HashMap<>();
        response.put("fullName", user.getFullName());
        response.put("role", user.getRole());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/applications/create")
    public ResponseEntity CreateApplication(@RequestHeader("Authorization") String token,
                                            @RequestBody ApplicationDto appDto) {
        var app = appDto.toApplication();
        var email = jwtTokenProvider.getUsername(token.substring(4));
        var user = userService.findByEmail(email);
        app.setAuthor(user);
        applicationService.save(app);
        return ResponseEntity.ok(app);
    }

    @PostMapping("/applications/edit")
    public ResponseEntity EditApplication(@RequestHeader("Authorization") String token,
                                          @RequestParam(name = "id") Long id,
                                          @RequestBody ApplicationDto appDto) {
        var app = applicationService.findById(id);
        if (app == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        var email = jwtTokenProvider.getUsername(token.substring(4));
        var user = userService.findByEmail(email);
        if (!app.authorId().equals(user.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        app = appDto.toApplication(app);
        applicationService.save(app);
        return ResponseEntity.ok(app);
    }

    @GetMapping("/applications/vote")
    public ResponseEntity VoteApplication(@RequestHeader("Authorization") String token,
                                          @RequestParam(name = "id") Long id) {
        var app = applicationService.findById(id);
        if (app == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (app.getAppStatus() != ApplicationStatus.VOTE)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        var email = jwtTokenProvider.getUsername(token.substring(4));
        var user = userService.findByEmail(email);
        if (app.getVotedUsers().contains(user))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        var userVotes = user.getVotes();
        userVotes.add(app);
        user.setVotes(userVotes);
        userService.save(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/applications/getAll")
    public ResponseEntity GetAllApplications(@RequestHeader("Authorization") String token) {
        var email = jwtTokenProvider.getUsername(token.substring(4));
        var user = userService.findByEmail(email);
        return ResponseEntity.ok(applicationService.getAll().stream().filter(x ->
                (user.hasRole(Role.MODERATOR))
                        || x.getAppStatus() != ApplicationStatus.NEW || x.getAuthor().getId().equals(user.getId())));
    }
}