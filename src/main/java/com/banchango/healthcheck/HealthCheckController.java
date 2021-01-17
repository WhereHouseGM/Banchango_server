package com.banchango.healthcheck;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/health-check")
    public String responseToHealthCheck() {
        return "I AM REALLY HEALTHY!";
    }
}