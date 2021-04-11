package com.greenorine.innovationsurfuapi.rest;

import com.greenorine.innovationsurfuapi.model.Application;
import com.greenorine.innovationsurfuapi.model.ApplicationStatus;
import com.greenorine.innovationsurfuapi.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/expert/")
public class ExpertRestControllerV1 {

    private ApplicationService applicationService;

    public ExpertRestControllerV1(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/vote")
    public ResponseEntity<Application> VoteApplication(@RequestParam(name = "id") Long id, @RequestBody String expertComment) {
        var app = applicationService.findById(id);
        if (app == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (app.getAppStatus() != ApplicationStatus.EXPERT_VOTE)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        app.setExpertComment(expertComment);
        applicationService.save(app);
        return ResponseEntity.noContent().build();
    }
}
