package com.banchango.healthcheck;

import com.banchango.ApiTestContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthCheckTest extends ApiTestContext {
    @Test
    public void healthCheckIsOK() {
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create("/health-check")).build();
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(requestEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
