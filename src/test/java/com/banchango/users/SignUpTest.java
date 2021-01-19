package com.banchango.users;

import com.banchango.ApiTestContext;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.request.UserSignupRequestFactory;
import com.banchango.users.dto.UserInfoResponseDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.UserEmailNotFoundException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SignUpTest extends ApiTestContext {

    @Autowired
    private UserEntityFactory userEntityFactory;

    @Autowired
    private UsersRepository usersRepository;

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
        Users user = userEntityFactory.createUserWithOwnerType();

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
}
