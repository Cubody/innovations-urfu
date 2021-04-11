package com.greenorine.innovationsurfuapi.service.implementation;

import com.greenorine.innovationsurfuapi.model.Application;
import com.greenorine.innovationsurfuapi.repository.ApplicationRepository;
import com.greenorine.innovationsurfuapi.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void save(Application application) {
        applicationRepository.save(application);

        log.info("IN save: application {} successfully saved", application);
    }

    @Override
    public List<Application> getAll() {
        var apps = applicationRepository.findAll();
        log.info("IN getAll: found {} applications", apps.size());
        return apps;
    }

    @Override
    public Application findById(Long id) {
        var app = applicationRepository.findById(id).orElse(null);

        if (app == null) {
            log.info("IN findById: no application found by id {}", id);
            return null;
        }

        log.info("IN findById: application {} found by id {}", app, id);
        return app;
    }

    @Override
    public void delete(Long id) {
        applicationRepository.deleteById(id);
        log.info("IN delete: application with id {} deleted", id);
    }
}
