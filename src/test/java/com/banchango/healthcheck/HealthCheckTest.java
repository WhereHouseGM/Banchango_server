package com.banchango.healthcheck;

import com.banchango.ApiTestContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthCheckTest extends ApiTestContext {
    @Test
    public void healthCheckIsOK() {
        String response = this.restTemplate.getForObject("/health-check", String.class);
        assertThat(response).containsIgnoringCase("I AM HEALTHY");
    }
}
