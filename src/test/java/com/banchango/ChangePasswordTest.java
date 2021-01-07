package com.banchango;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.users.dto.ChangePasswordRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// TODO: 작업 중인거 다 머지 되면 UserApiTest로 이동
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ChangePasswordTest extends ApiTestContext {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserEntityFactory userEntityFactory;

    String accessToken = null;
    Users user = null;

    private final String VALID_PASSWORD = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
    private final String INVALID_PASSWORD = "test";

    @Before
    public void beforeTest() {
        usersRepository.deleteAll();

        user = userEntityFactory.createUser();
        accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
    }

    @After
    public void afterTest() {
        usersRepository.deleteAll();
    }

    @Test
    public void patch_passwordChange_responseIsOk_IfAllConditionsAreRight() {
        String originalPassword = user.getPassword();
        String newPassword = VALID_PASSWORD;

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(user.getPassword(), newPassword);

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
        String originalPassword = user.getPassword();
        String newPassword = VALID_PASSWORD;

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto("744ea9ec6fa0a83e9764b4e323d5be6b55a5accfc7fe4c08eab6a8de1fca4855", newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(changePasswordRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void patch_passwordChange_responseIsBadRequest_IfNewPasswordLengthIsNot64() {
        String originalPassword = user.getPassword();
        String newPassword = INVALID_PASSWORD;

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(user.getPassword(), newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .body(changePasswordRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void patch_changePassword_responseIsUnauthorized_IfAccessTokenNotGiven() {
        String originalPassword = user.getPassword();
        String newPassword = VALID_PASSWORD;

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(user.getPassword(), newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(changePasswordRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void patch_changePassword_responseNoContent_IfUserIdIsInvalid() {
        String originalPassword = user.getPassword();
        String newPassword = VALID_PASSWORD;
        String accessTokenWithInvalidUserId = JwtTokenUtil.generateAccessToken(0, UserRole.USER);

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(user.getPassword(), newPassword);

        RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessTokenWithInvalidUserId)
            .body(changePasswordRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}