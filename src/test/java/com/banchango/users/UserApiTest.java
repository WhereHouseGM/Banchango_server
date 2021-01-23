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

    private static final String WRONG_EMAIL = "WRONG_EMAIL";
    private static final String WRONG_PASSWORD = "WRONG_PASSWORD";

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
    public void signIn_responseIsOK_IfUserExists() {
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

        ResponseEntity<UserSigninResponseDto> response = restTemplate.exchange(request, UserSigninResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void signIn_responseIsNotFound_IfUserPasswordIsWrong() {

        UserSigninRequestDto requestBody = new UserSigninRequestDto(user.getEmail(), WRONG_PASSWORD);

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-in"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody);

        ResponseEntity<UserSigninResponseDto> response = restTemplate.exchange(request, UserSigninResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void signIn_responseIsNotFound_IfUserIsWithdrawm(){
        Users deletedUser = userEntityFactory.createDeletedUser();
        UserSigninRequestDto requestBody = new UserSigninRequestDto(deletedUser.getEmail(), deletedUser.getPassword());

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-in"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody);

        ResponseEntity<UserSigninResponseDto> response = restTemplate.exchange(request, UserSigninResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void signIn_responseIsBadRequest_IfRequestBodyIsWrong() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", user.getEmail());
        requestBody.put("pass", user.getPassword());

        RequestEntity<String> request = RequestEntity.post(URI.create("/v3/users/sign-up"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody.toString());

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void signUp_responseIsOK() {
        UserSignupRequestDto requestBody = UserSignupRequestFactory.createNewUser();

        RequestEntity<UserSignupRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-up"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        Users savedUser = usersRepository.findByEmail(requestBody.getEmail()).orElseThrow(UserEmailNotFoundException::new);
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
    }

    @Test
    public void signUp_responseIsConflict_IfEmailExists() {

        UserSignupRequestDto requestBody = UserSignupRequestFactory.createDuplicateUser(user.getEmail());

        RequestEntity<UserSignupRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-up"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

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

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
        warehouses
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
