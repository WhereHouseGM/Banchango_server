package com.banchango.users;

import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.users.exception.UserEmailNotFoundException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiTest {

    @Before
    public void saveUser() {
        Users user = Users.builder().name("TEST_NAME")
                .email("TEST_EMAIL")
                .password("123")
                .userType(UserType.OWNER)
                .telephoneNumber("010123123")
                .phoneNumber("02123123")
                .companyName("TEST_COMP")
                .build();
        usersRepository.save(user);
    }

    @After
    public void removeUser() {
        Users user = usersRepository.findByEmail("TEST_EMAIL").orElseThrow(UserEmailNotFoundException::new);
        usersRepository.delete(user);
    }

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void loginTestIfUserExists() {

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "TEST_EMAIL");
        requestBody.put("password", "123");

        ResponseEntity<String> response = getResponse(requestBody.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("accessToken"));
        assertTrue(response.getBody().contains("refreshToken"));
    }

    @Test
    public void loginTestIfUserEmailIsWrong(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "WRONG_EMAIL");
        requestBody.put("password", "123");

        ResponseEntity<String> response = getResponse(requestBody.toString());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void loginTestIfUserPasswordIsWrong() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "TEST_EMAIL");
        requestBody.put("password", "1234");

        ResponseEntity<String> response = getResponse(requestBody.toString());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    @Test
    public void loginTestIfRequestBodyIsWrong() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "TEST_EMAIL");
        requestBody.put("pass", "123");

        ResponseEntity<String> response = getResponse(requestBody.toString());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private ResponseEntity<String> getResponse(String requestBody) {
        RequestEntity<String> request = RequestEntity.post(URI.create("/v2/users/sign-in"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody.toString());

        return restTemplate.exchange(request, String.class);
    }
}
