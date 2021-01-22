package com.banchango.users;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.factory.request.UserUpdateRequestFactory;
import com.banchango.users.dto.UserInfoResponseDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.dto.UserUpdateRequestDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class UpdateUserTest extends ApiIntegrationTest {

    @Test
    public void updateInfo_responseIsOk_IfUserIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);
        Integer userId = user.getUserId();

        UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();


        RequestEntity<UserUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserInfoResponseDto responseBody = response.getBody();

        assertEquals(requestBody.getName(), responseBody.getName());
        assertEquals(UserRole.USER, responseBody.getRole());
        assertEquals(requestBody.getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(requestBody.getTelephoneNumber(), responseBody.getTelephoneNumber());
        assertEquals(requestBody.getCompanyName(), responseBody.getCompanyName());

        RequestEntity<Void> secondRequest = RequestEntity.get(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<UserInfoResponseDto> secondResponse = restTemplate.exchange(secondRequest, UserInfoResponseDto.class);
        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
        assertEquals(UserUpdateRequestFactory.NEW_NAME, secondResponse.getBody().getName());
        assertEquals(UserRole.USER, secondResponse.getBody().getRole());
        assertEquals(UserUpdateRequestFactory.NEW_TELEPHONE_NUMBER, secondResponse.getBody().getTelephoneNumber());
        assertEquals(UserUpdateRequestFactory.NEW_PHONE_NUMBER, secondResponse.getBody().getPhoneNumber());
        assertEquals(UserUpdateRequestFactory.NEW_COMP_NAME, secondResponse.getBody().getCompanyName());
    }

    @Test
    public void updateInfo_responseIsOk_IfUserIsShipper() {
        Users user = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);
        Integer userId = user.getUserId();

        UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();


        RequestEntity<UserUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserInfoResponseDto responseBody = response.getBody();

        assertEquals(requestBody.getName(), responseBody.getName());
        assertEquals(UserRole.USER, responseBody.getRole());
        assertEquals(requestBody.getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(requestBody.getTelephoneNumber(), responseBody.getTelephoneNumber());
        assertEquals(requestBody.getCompanyName(), responseBody.getCompanyName());

        RequestEntity<Void> secondRequest = RequestEntity.get(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        ResponseEntity<UserInfoResponseDto> secondResponse = restTemplate.exchange(secondRequest, UserInfoResponseDto.class);
        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
        assertEquals(UserUpdateRequestFactory.NEW_NAME, secondResponse.getBody().getName());
        assertEquals(UserRole.USER, secondResponse.getBody().getRole());
        assertEquals(UserUpdateRequestFactory.NEW_TELEPHONE_NUMBER, secondResponse.getBody().getTelephoneNumber());
        assertEquals(UserUpdateRequestFactory.NEW_PHONE_NUMBER, secondResponse.getBody().getPhoneNumber());
        assertEquals(UserUpdateRequestFactory.NEW_COMP_NAME, secondResponse.getBody().getCompanyName());
    }

    @Test
    public void updateInfo_responseIsUnAuthorized_IfTokenIsMalformed() {
        Users user = userEntityFactory.createUserWithOwnerType();
        Integer userId = user.getUserId();

        UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();

        RequestEntity<UserUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + "THIS IS WRONG TOKEN!")
                .contentType(MediaType.APPLICATION_JSON).body(requestBody);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void updateInfo_responseIsUnAuthorized_IfUserIdAndTokenIsWrong() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);
        Integer userId = user.getUserId();

        UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();

        RequestEntity<UserUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/users/0"))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void updateInfo_responseIsNotFound_IfUserIdIsWrong() {
        String accessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER, UserType.OWNER);

        UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();

        RequestEntity<UserUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/users/0"))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
