package com.banchango.users;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.request.UserSignupRequestFactory;
import com.banchango.factory.request.UserUpdateRequestFactory;
import com.banchango.users.dto.*;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserApiTest {

    @Autowired
    private UserEntityFactory userEntityFactory;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WarehousesRepository warehousesRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private Users user = null;

    @Before
    public void saveUser() {
        usersRepository.deleteAll();
        user = userEntityFactory.createUser();
    }

    @After
    public void removeUser() {
        usersRepository.deleteAll();
    }

    @Test
    public void userInfo_responseIsOk_IfAllConditionsAreRight() {
        Integer userId = user.getUserId();
        String accessToken = JwtTokenUtil.generateAccessToken(userId, UserRole.USER);
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
        Integer userId = user.getUserId();
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/" + userId)).build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void userInfo_responseIsUnAuthorized_IfTokenIsMalformed() {
        Integer userId = user.getUserId();
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/" + userId)).build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void userInfo_responseIsNotFound_IfUserIdIsWrong() {
        String accessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER);
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/0"))
                .header("Authorization", "Bearer " + accessToken).build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void updateInfo_responseIsOk() {
        UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();

        Integer userId = user.getUserId();
        String accessToken = JwtTokenUtil.generateAccessToken(userId, UserRole.USER);

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
        UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();

        Integer userId = user.getUserId();

        RequestEntity<UserUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + "THIS IS WRONG TOKEN!")
                .contentType(MediaType.APPLICATION_JSON).body(requestBody);

        ResponseEntity<UserSignupRequestDto> response = restTemplate.exchange(request, UserSignupRequestDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void updateInfo_responseIsUnAuthorized_IfUserIdAndTokenIsWrong() {
        UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();

        Integer userId = user.getUserId();
        String accessToken = JwtTokenUtil.generateAccessToken(userId, UserRole.USER);

        RequestEntity<UserUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/users/0"))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void updateInfo_responseIsNotFound_IfUserIdIsWrong() {
        UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();

        String accessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER);

        RequestEntity<UserUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/users/0"))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void withdrawUser_responseIsOk_IfAllConditionsAreRight() {
        Users userToDelete = userEntityFactory.createUser();
        String accessTokenForUserToDelete = JwtTokenUtil.generateAccessToken(userToDelete.getUserId(), userToDelete.getRole());
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+userToDelete.getUserId()+"/withdraw"))
            .header("Authorization", "Bearer " + accessTokenForUserToDelete)
            .body(userWithdrawRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        List<Warehouses> warehouses = warehousesRepository.findByUserId(userToDelete.getUserId());
        warehouses.stream()
            .forEach(warehouse -> assertEquals(WarehouseStatus.DELETED, warehouse.getStatus()));
    }

    @Test
    public void withdrawUser_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        Users userToDelete = userEntityFactory.createUser();
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+userToDelete.getUserId()+"/withdraw"))
            .body(userWithdrawRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void withdrawUser_responseIsForbidden_IfGivenOtherUserId() {
        Users userToDelete = userEntityFactory.createUser();
        Users otherUserToDelete = userEntityFactory.createUser();
        String accessTokenForUserToDelete = JwtTokenUtil.generateAccessToken(userToDelete.getUserId(), userToDelete.getRole());
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+otherUserToDelete.getUserId()+"/withdraw"))
            .header("Authorization", "Bearer " + accessTokenForUserToDelete)
            .body(userWithdrawRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    public void withdrawUser_responseIsNotFound_IfUserNotExist() {
        int userId = 0;
        String accessTokenForUserToDelete = JwtTokenUtil.generateAccessToken(userId, UserRole.USER);
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+userId+"/v3/users/"+userId+"/withdraw"+"/withdraw"))
            .header("Authorization", "Bearer " + accessTokenForUserToDelete)
            .body(userWithdrawRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void withdrawUser_responseIsConflict_IfUserAlreadyDeleted() {
        Users deletedUser = userEntityFactory.createDeletedUser();
        String accessTokenForUserToDelete = JwtTokenUtil.generateAccessToken(deletedUser.getUserId(), deletedUser.getRole());
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+deletedUser.getUserId()+"/withdraw"))
            .header("Authorization", "Bearer " + accessTokenForUserToDelete)
            .body(userWithdrawRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
