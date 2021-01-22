package com.banchango.healthcheck;

import com.banchango.ApiIntegrationTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class HealthCheckTest extends ApiIntegrationTest {
    @Test
    public void healthCheckIsOK() {
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create("/health-check")).build();
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(requestEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
