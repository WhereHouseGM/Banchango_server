package com.banchango.users;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.users.dto.UserInfoResponseDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class GetUserTest extends ApiIntegrationTest {

    @Test
    public void userInfo_responseIsOk_IfUserIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        Integer userId = user.getUserId();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + accessToken).build();
        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        UserInfoResponseDto responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getEmail(), responseBody.getEmail());
        assertEquals(userId, responseBody.getUserId());
        assertEquals(user.getName(), responseBody.getName());
        assertEquals(user.getType(), responseBody.getType());
        assertEquals(user.getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(user.getCompanyName(), responseBody.getCompanyName());
        assertEquals(user.getTelephoneNumber(), responseBody.getTelephoneNumber());
        assertEquals(false, responseBody.getIsDeleted());
    }

    @Test
    public void userInfo_responseIsOk_IfUserIsShipper() {
        Users user = userEntityFactory.createUserWithShipperType();
        Integer userId = user.getUserId();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + accessToken).build();
        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        UserInfoResponseDto responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getEmail(), responseBody.getEmail());
        assertEquals(userId, responseBody.getUserId());
        assertEquals(user.getName(), responseBody.getName());
        assertEquals(user.getType(), responseBody.getType());
        assertEquals(user.getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(user.getCompanyName(), responseBody.getCompanyName());
        assertEquals(user.getTelephoneNumber(), responseBody.getTelephoneNumber());
    }

    @Test
    public void userInfo_responseIsUnAuthorized_IfTokenIsAbsent() {
        Users user = userEntityFactory.createUserWithOwnerType();
        Integer userId = user.getUserId();
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/" + userId)).build();
        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void userInfo_responseIsUnAuthorized_IfTokenIsMalformed() {
        Users user = userEntityFactory.createUserWithOwnerType();
        Integer userId = user.getUserId();
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/" + userId)).build();
        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void userInfo_responseIsNotFound_IfUserIdIsWrong() {
        String accessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER, UserType.OWNER);

        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/0"))
                .header("Authorization", "Bearer " + accessToken).build();
        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
