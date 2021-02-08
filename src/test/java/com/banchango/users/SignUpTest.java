package com.banchango.users;

import com.banchango.ApiIntegrationTest;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.User;
import com.banchango.factory.request.UserSignupRequestFactory;
import com.banchango.users.dto.UserInfoResponseDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.UserEmailNotFoundException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class SignUpTest extends ApiIntegrationTest {

    @Test
    public void signUp_responseIsOK() {
        UserSignupRequestDto requestBody = UserSignupRequestFactory.createNewUser();

        RequestEntity<UserSignupRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-up"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        User savedUser = userRepository.findByEmail(requestBody.getEmail()).orElseThrow(UserEmailNotFoundException::new);
        Integer userId = savedUser.getUserId();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserInfoResponseDto responseBody = response.getBody();

        assertEquals(requestBody.getEmail(), responseBody.getEmail());
        assertEquals(userId, responseBody.getUserId());
        assertEquals(requestBody.getName(), responseBody.getName());
        assertEquals(requestBody.getEmail(), responseBody.getEmail());
        assertEquals(requestBody.getType(), responseBody.getType());
        assertEquals(requestBody.getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(requestBody.getTelephoneNumber(), responseBody.getTelephoneNumber());
        assertEquals(requestBody.getCompanyName(), responseBody.getCompanyName());

        assertTrue(savedUser.getCreatedAt().isBefore(LocalDateTime.now()));
        assertTrue(savedUser.getLastModifiedAt().isBefore(LocalDateTime.now()));
        assertNotNull(findUserById.apply(savedUser.getUserId()));
    }

    @Test
    public void signUp_responseIsConflict_IfEmailExists() {
        User user = userEntityFactory.createUserWithOwnerType();

        UserSignupRequestDto requestBody = UserSignupRequestFactory.createDuplicateUser(user.getEmail());

        RequestEntity<UserSignupRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-up"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void signUp_responseIsBadRequest_IfRequestBodyIsWrong() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "TEST_NAME");
        requestBody.put("email", "TEST_EMAIL_");
        requestBody.put("password", "1234");
        requestBody.put("type", "WRONG_TYPE");
        requestBody.put("phoneNumber", "010234234");
        requestBody.put("companyName", "companyName");

        RequestEntity<String> request = RequestEntity.post(URI.create("/v3/users/sign-up"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody.toString());

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
