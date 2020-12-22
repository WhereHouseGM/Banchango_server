package com.banchango.healthcheck;

import com.banchango.ApiTestContext;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckTest extends ApiTestContext {
    @Test
    public void healthCheckIsOK() {
        String response = this.restTemplate.getForObject("/health-check", String.class);
        assertThat(response).containsIgnoringCase("I AM HEALTHY");
    }
}
