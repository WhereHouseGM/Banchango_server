package com.banchango.users;

import com.banchango.ApiIntegrationTest;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.Users;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSigninResponseDto;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SignInTest extends ApiIntegrationTest {

    private static final String WRONG_EMAIL = "WRONG_EMAIL";
    private static final String WRONG_PASSWORD = "WRONG_PASSWORD";

    @Test
    public void signIn_responseIsOK_IfUserExists() {
        Users user = userEntityFactory.createUserWithOwnerType();

        UserSigninRequestDto requestBody = new UserSigninRequestDto(user.getEmail(), user.getPassword());

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-in"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserSigninResponseDto> response = restTemplate.exchange(request, UserSigninResponseDto.class);

        UserSigninResponseDto responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody.getAccessToken());
        assertNotNull(responseBody.getRefreshToken());
        assertEquals("Bearer", responseBody.getTokenType() );

        assertNotNull(responseBody.getUser());
        assertEquals(user.getUserId(), responseBody.getUser().getUserId());
        assertEquals(user.getName(), responseBody.getUser().getName());
        assertEquals(user.getEmail(), responseBody.getUser().getEmail());
        assertEquals(user.getPhoneNumber(), responseBody.getUser().getPhoneNumber());
        assertEquals(user.getType(), responseBody.getUser().getType());
        assertEquals(user.getTelephoneNumber(), responseBody.getUser().getTelephoneNumber());
        assertEquals(user.getCompanyName(), responseBody.getUser().getCompanyName());
        assertEquals(user.getRole(), responseBody.getUser().getRole());
    }

    @Test
    public void signIn_responseIsNotFound_IfUserEmailIsWrong(){
        UserSigninRequestDto requestBody = new UserSigninRequestDto(WRONG_EMAIL, WRONG_PASSWORD);

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-in"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void signIn_responseIsNotFound_IfUserPasswordIsWrong() {
        Users user = userEntityFactory.createUserWithOwnerType();
        UserSigninRequestDto requestBody = new UserSigninRequestDto(user.getEmail(), WRONG_PASSWORD);

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-in"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void signIn_responseIsNotFound_IfUserIsWithdrawn(){
        Users deletedUser = userEntityFactory.createDeletedUserWithOwnerType();
        UserSigninRequestDto requestBody = new UserSigninRequestDto(deletedUser.getEmail(), deletedUser.getPassword());

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-in"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void signIn_responseIsBadRequest_IfRequestBodyIsWrong() {
        Users user = userEntityFactory.createUserWithOwnerType();

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", user.getEmail());
        requestBody.put("pass", user.getPassword());

        RequestEntity<String> request = RequestEntity.post(URI.create("/v3/users/sign-up"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody.toString());

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
