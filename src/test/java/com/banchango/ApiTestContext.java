package com.banchango;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Rollback
@Transactional
public abstract class ApiTestContext {
    @Autowired
    protected TestRestTemplate restTemplate;

    protected ResponseEntity<String> getResponse(String requestBody, String url) {
        RequestEntity<String> request = RequestEntity.post(URI.create(url))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        return restTemplate.exchange(request, String.class);
    }
}
