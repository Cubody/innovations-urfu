package com.greenorine.innovationsurfuapi.rest;

import com.greenorine.innovationsurfuapi.dto.ApplicationDto;
import com.greenorine.innovationsurfuapi.model.ApplicationStatus;
import com.greenorine.innovationsurfuapi.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/moderator/")
public class ModeratorRestControllerV1 {

    private ApplicationService applicationService;

    public ModeratorRestControllerV1(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/edit")
    public ResponseEntity EditApplication(@RequestParam(name = "id") Long id,
                                          @RequestBody ApplicationDto appDto) {
        var app = applicationService.findById(id);
        if (app == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        app = appDto.toApplication(app);
        applicationService.save(app);
        return ResponseEntity.ok(app);
    }

    @GetMapping("/changeStatus")
    public ResponseEntity ChangeApplicationStatus(@RequestParam(name = "id") Long id,
                                                  @RequestParam(name = "appStatus") ApplicationStatus appStatus) {
        var app = applicationService.findById(id);
        if (app == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        app.setAppStatus(appStatus);
        applicationService.save(app);
        return ResponseEntity.noContent().build();
    }
}
