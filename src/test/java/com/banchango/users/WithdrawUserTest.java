package com.banchango.users;

import com.banchango.ApiIntegrationTest;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.users.dto.UserWithdrawRequestDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

public class WithdrawUserTest extends ApiIntegrationTest {

    @Test
    public void withdrawUser_responseIsOk_IfUserIsOwner() {
        Users userToDelete = userEntityFactory.createUserWithOwnerType();
        String accessTokenForUserToDelete = JwtTokenUtil.generateAccessToken(userToDelete);
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+userToDelete.getUserId()+"/withdraw"))
            .header("Authorization", "Bearer " + accessTokenForUserToDelete)
            .body(userWithdrawRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        List<Warehouses> warehouses = warehousesRepository.findByUserId(userToDelete.getUserId());
        warehouses.forEach(warehouse -> assertEquals(WarehouseStatus.DELETED, warehouse.getStatus()));
        assertTrue(usersRepository.findById(userToDelete.getUserId()).isPresent());
        assertTrue(withdrawsRepository.findByUserId(userToDelete.getUserId()).isPresent());
    }

    @Test
    public void withdrawUser_responseIsOk_IfUserIsShipper() {
        Users userToDelete = userEntityFactory.createUserWithShipperType();
        String accessTokenForUserToDelete = JwtTokenUtil.generateAccessToken(userToDelete);
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+userToDelete.getUserId()+"/withdraw"))
                .header("Authorization", "Bearer " + accessTokenForUserToDelete)
                .body(userWithdrawRequestDto);

        ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());

        List<Warehouses> warehouses = warehousesRepository.findByUserId(userToDelete.getUserId());
        warehouses.forEach(warehouse -> assertEquals(WarehouseStatus.DELETED, warehouse.getStatus()));
        assertTrue(usersRepository.findById(userToDelete.getUserId()).isPresent());
        assertTrue(withdrawsRepository.findByUserId(userToDelete.getUserId()).isPresent());
    }

    @Test
    public void withdrawUser_responseIsUnAuthorized_IfAccessTokenNotGiven() {
        Users userToDelete = userEntityFactory.createUserWithOwnerType();
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+userToDelete.getUserId()+"/withdraw"))
            .body(userWithdrawRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void withdrawUser_responseIsForbidden_IfGivenOtherUserId() {
        Users userToDelete = userEntityFactory.createUserWithOwnerType();
        Users otherUserToDelete = userEntityFactory.createUserWithOwnerType();
        String accessTokenForUserToDelete = JwtTokenUtil.generateAccessToken(userToDelete);
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+otherUserToDelete.getUserId()+"/withdraw"))
            .header("Authorization", "Bearer " + accessTokenForUserToDelete)
            .body(userWithdrawRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    public void withdrawUser_responseIsNotFound_IfUserNotExist() {
        int userId = 0;
        String accessTokenForUserToDelete = JwtTokenUtil.generateAccessToken(userId, UserRole.USER, UserType.OWNER);
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+userId+"/v3/users/"+userId+"/withdraw"+"/withdraw"))
            .header("Authorization", "Bearer " + accessTokenForUserToDelete)
            .body(userWithdrawRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void withdrawUser_responseIsConflict_IfUserAlreadyDeleted() {
        Users deletedUser = userEntityFactory.createDeletedUserWithOwnerType();
        String accessTokenForUserToDelete = JwtTokenUtil.generateAccessToken(deletedUser);
        UserWithdrawRequestDto userWithdrawRequestDto = new UserWithdrawRequestDto("탈퇴 사유");

        RequestEntity<UserWithdrawRequestDto> request = RequestEntity.post(URI.create("/v3/users/"+deletedUser.getUserId()+"/withdraw"))
            .header("Authorization", "Bearer " + accessTokenForUserToDelete)
            .body(userWithdrawRequestDto);

        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(request, ErrorResponseDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
