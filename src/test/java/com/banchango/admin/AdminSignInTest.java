package com.banchango.admin;

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

public class AdminSignInTest extends ApiIntegrationTest {

    private static final String WRONG_EMAIL = "WRONG_EMAIL";
    private static final String WRONG_PASSWORD = "WRONG_PASSWORD";

    @Test
    public void signIn_responseIsOk_IfUserIsAdminAndOwner() {
        Users adminOwner = userEntityFactory.createAdminWithOwnerType();

        UserSigninRequestDto requestBody = new UserSigninRequestDto(adminOwner.getEmail(), adminOwner.getPassword());

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/admin/users/sign-in"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserSigninResponseDto> response = restTemplate.exchange(request, UserSigninResponseDto.class);

        UserSigninResponseDto responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody.getAccessToken());
        assertNotNull(responseBody.getRefreshToken());
        assertEquals("Bearer", responseBody.getTokenType() );

        assertNotNull(responseBody.getUser());
        assertEquals(adminOwner.getUserId(), responseBody.getUser().getUserId());
        assertEquals(adminOwner.getName(), responseBody.getUser().getName());
        assertEquals(adminOwner.getEmail(), responseBody.getUser().getEmail());
        assertEquals(adminOwner.getPhoneNumber(), responseBody.getUser().getPhoneNumber());
        assertEquals(adminOwner.getType(), responseBody.getUser().getType());
        assertEquals(adminOwner.getTelephoneNumber(), responseBody.getUser().getTelephoneNumber());
        assertEquals(adminOwner.getCompanyName(), responseBody.getUser().getCompanyName());
        assertEquals(adminOwner.getRole(), responseBody.getUser().getRole());
    }

    @Test
    public void signIn_responseIsOk_IfUserIsAdminAndShipper() {
        Users adminShipper = userEntityFactory.createAdminWithShipperType();

        UserSigninRequestDto requestBody = new UserSigninRequestDto(adminShipper.getEmail(), adminShipper.getPassword());

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/admin/users/sign-in"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserSigninResponseDto> response = restTemplate.exchange(request, UserSigninResponseDto.class);

        UserSigninResponseDto responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody.getAccessToken());
        assertNotNull(responseBody.getRefreshToken());
        assertEquals("Bearer", responseBody.getTokenType() );

        assertNotNull(responseBody.getUser());
        assertEquals(adminShipper.getUserId(), responseBody.getUser().getUserId());
        assertEquals(adminShipper.getName(), responseBody.getUser().getName());
        assertEquals(adminShipper.getEmail(), responseBody.getUser().getEmail());
        assertEquals(adminShipper.getPhoneNumber(), responseBody.getUser().getPhoneNumber());
        assertEquals(adminShipper.getType(), responseBody.getUser().getType());
        assertEquals(adminShipper.getTelephoneNumber(), responseBody.getUser().getTelephoneNumber());
        assertEquals(adminShipper.getCompanyName(), responseBody.getUser().getCompanyName());
        assertEquals(adminShipper.getRole(), responseBody.getUser().getRole());
    }

    @Test
    public void signIn_responseIsBadRequest_IfRequestBodyIsWrong() {
        Users user = userEntityFactory.createUserWithOwnerType();

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", user.getEmail());
        requestBody.put("pass", user.getPassword());

        RequestEntity<String> request = RequestEntity.post(URI.create("/v3/admin/users/sign-up"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody.toString());

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void signIn_responseIsNotFound_IfUserIsNotAdmin() {
        Users user = userEntityFactory.createUserWithOwnerType();

        UserSigninRequestDto requestBody = new UserSigninRequestDto(user.getEmail(), user.getPassword());

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/admin/users/sign-in"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void signIn_responseIsNotFound_IfUserNotExist() {
        UserSigninRequestDto requestBody = new UserSigninRequestDto(WRONG_EMAIL, WRONG_PASSWORD);

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/admin/users/sign-in"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
