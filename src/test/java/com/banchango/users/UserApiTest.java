package com.banchango.users;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.request.UserSignupRequestFactory;
import com.banchango.users.dto.UserInfoResponseDto;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSigninResponseDto;
import com.banchango.users.dto.UserSignupRequestDto;
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
    public void userInfo_responseIsNoContent_IfUserIdIsWrong() {
        String accessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER);
        RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/0"))
                .header("Authorization", "Bearer " + accessToken).build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
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
        assertEquals(user.getEmail(), responseBody.getUser().getEmail());
        assertEquals(user.getUserId(), responseBody.getUser().getUserId());
        assertEquals(user.getName(), responseBody.getUser().getName());
        assertEquals(user.getType(), responseBody.getUser().getType());
        assertEquals(user.getPhoneNumber(), responseBody.getUser().getPhoneNumber());
        assertEquals(user.getCompanyName(), responseBody.getUser().getCompanyName());
    }

    @Test
    public void signIn_responseIsNoContent_IfUserEmailIsWrong(){
        UserSigninRequestDto requestBody = new UserSigninRequestDto(WRONG_EMAIL, WRONG_PASSWORD);

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-in"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody);

        ResponseEntity<UserSigninResponseDto> response = restTemplate.exchange(request, UserSigninResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void signIn_responseIsNoContent_IfUserPasswordIsWrong() {

        UserSigninRequestDto requestBody = new UserSigninRequestDto(user.getEmail(), WRONG_PASSWORD);

        RequestEntity<UserSigninRequestDto> request = RequestEntity.post(URI.create("/v3/users/sign-in"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody);

        ResponseEntity<UserSigninResponseDto> response = restTemplate.exchange(request, UserSigninResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
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
        assertEquals(requestBody.getType(), responseBody.getType());
        assertEquals(requestBody.getPhoneNumber(), responseBody.getPhoneNumber());
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
        UserSignupRequestDto requestBody = UserSignupRequestFactory.createNewUser();

        Integer userId = user.getUserId();
        String accessToken = JwtTokenUtil.generateAccessToken(userId, UserRole.USER);

        RequestEntity<UserSignupRequestDto> request = RequestEntity.patch(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserInfoResponseDto responseBody = response.getBody();

        assertEquals(requestBody.getName(), responseBody.getName());
        assertEquals(requestBody.getType(), responseBody.getType());
        assertEquals(requestBody.getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(requestBody.getCompanyName(), responseBody.getCompanyName());
        assertEquals(requestBody.getEmail(), responseBody.getEmail());
        assertEquals(requestBody.getTelephoneNumber(), responseBody.getTelephoneNumber());
    }

    @Test
    public void updateInfo_responseIsUnAuthorized_IfTokenIsMalformed() {
        UserSignupRequestDto requestBody = UserSignupRequestFactory.createNewUser();

        Integer userId = user.getUserId();

        RequestEntity<UserSignupRequestDto> request = RequestEntity.patch(URI.create("/v3/users/" + userId))
                .header("Authorization", "Bearer " + "THIS IS WRONG TOKEN!")
                .contentType(MediaType.APPLICATION_JSON).body(requestBody);

        ResponseEntity<UserSignupRequestDto> response = restTemplate.exchange(request, UserSignupRequestDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void updateInfo_responseIsUnAuthorized_IfUserIdAndTokenIsWrong() {
        UserSignupRequestDto requestBody = UserSignupRequestFactory.createNewUser();

        Integer userId = user.getUserId();
        String accessToken = JwtTokenUtil.generateAccessToken(userId, UserRole.USER);

        RequestEntity<UserSignupRequestDto> request = RequestEntity.patch(URI.create("/v3/users/0"))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void updateInfo_responseIsNoContent_IfUserIdIsWrong() {
        UserSignupRequestDto requestBody = UserSignupRequestFactory.createNewUser();

        String accessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER);

        RequestEntity<UserSignupRequestDto> request = RequestEntity.patch(URI.create("/v3/users/0"))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
