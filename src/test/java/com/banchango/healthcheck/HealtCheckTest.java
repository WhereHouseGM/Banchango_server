package com.banchango.healthcheck;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HealtCheckTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void healthCheckIsOK() {
        String response = this.restTemplate.getForObject("/health-check", String.class);
        assertThat(response).containsIgnoringCase("I AM HEALTHY");
    }
}
