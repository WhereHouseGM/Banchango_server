package com.banchango.users;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.users.dto.ChangePasswordRequestDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChangePasswordTest extends ApiIntegrationTest {

    private final String VALID_PASSWORD = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
    private final String INVALID_PASSWORD = "test";

    @Test
    public void patch_passwordChange_responseIsOk_IfUserIsOwner() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        String originalPassword = user.getPassword();
        String newPassword = VALID_PASSWORD;

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(originalPassword, newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(changePasswordRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    public void patch_passwordChange_responseIsOk_IfUserIsShipper() {
        Users user = userEntityFactory.createUserWithShipperType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        String originalPassword = user.getPassword();
        String newPassword = VALID_PASSWORD;

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(originalPassword, newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(changePasswordRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    public void patch_passwordChange_responseIsForbidden_IfOriginalPasswordNotMatch() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        String originalPassword = user.getPassword();
        String newPassword = VALID_PASSWORD;

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto("744ea9ec6fa0a83e9764b4e323d5be6b55a5accfc7fe4c08eab6a8de1fca4855", newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(changePasswordRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void patch_passwordChange_responseIsBadRequest_IfNewPasswordLengthIsNot64() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        String originalPassword = user.getPassword();
        String newPassword = INVALID_PASSWORD;

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(user.getPassword(), newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(changePasswordRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void patch_changePassword_responseIsUnauthorized_IfAccessTokenNotGiven() {
        Users user = userEntityFactory.createUserWithOwnerType();
        String accessToken = JwtTokenUtil.generateAccessToken(user);

        String originalPassword = user.getPassword();
        String newPassword = VALID_PASSWORD;

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(originalPassword, newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(changePasswordRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void patch_changePassword_responseNotFound_IfUserIdIsInvalid() {
        String originalPassword = VALID_PASSWORD;
        String newPassword = VALID_PASSWORD;
        String accessTokenWithInvalidUserId = JwtTokenUtil.generateAccessToken(0, UserRole.USER, UserType.OWNER);

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(originalPassword, newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessTokenWithInvalidUserId)
            .body(changePasswordRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}