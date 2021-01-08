package com.banchango;

import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.admin.dto.WarehouseAdminUpdateRequestDto;
import com.banchango.admin.dto.WarehouseInsertRequestResponseListDto;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouses.*;
import com.banchango.estimateitems.dto.EstimateItemSearchResponseDto;
import com.banchango.estimates.dto.EstimateInsertRequestDto;
import com.banchango.estimates.dto.EstimateSearchResponseDto;
import com.banchango.factory.entity.EstimateEntityFactory;
import com.banchango.factory.entity.EstimateItemEntityFactory;
import com.banchango.factory.entity.UserEntityFactory;
import com.banchango.factory.entity.WarehouseEntityFactory;
import com.banchango.factory.request.EstimatesInsertRequestFactory;
import com.banchango.factory.request.UserSignupRequestFactory;
import com.banchango.factory.request.UserUpdateRequestFactory;
import com.banchango.users.dto.*;
import com.banchango.users.exception.UserEmailNotFoundException;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import com.banchango.warehouses.dto.WarehouseSearchDto;
import com.banchango.warehouses.dto.WarehouseSearchResponseDto;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserEntityFactory userEntityFactory;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WarehousesRepository warehousesRepository;

    @Autowired
    private WarehouseEntityFactory warehouseEntityFactory;

    @Autowired
    private EstimateEntityFactory estimateEntityFactory;

    @Autowired
    private EstimatesRepository estimatesRepository;

    @Nested
    @DisplayName("User Api Tests.")
    public class UserApiTest {

        private static final String WRONG_EMAIL = "WRONG_EMAIL";
        private static final String WRONG_PASSWORD = "WRONG_PASSWORD";
        private Users user = null;
        private String accessToken;
        private final String VALID_PASSWORD = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        private final String INVALID_PASSWORD = "test";

        @BeforeEach
        public void saveUser() {
            usersRepository.deleteAll();
            user = userEntityFactory.createUser();
            accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
        }

        @AfterEach
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
        public void updateInfo_responseIsNoContent_IfUserIdIsWrong() {
            UserUpdateRequestDto requestBody = UserUpdateRequestFactory.create();

            String accessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER);

            RequestEntity<UserUpdateRequestDto> request = RequestEntity.patch(URI.create("/v3/users/0"))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody);

            ResponseEntity<UserInfoResponseDto> response = restTemplate.exchange(request, UserInfoResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void patch_passwordChange_responseIsOk_IfAllConditionsAreRight() {

            ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(user.getPassword(), VALID_PASSWORD);

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

            ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto("744ea9ec6fa0a83e9764b4e323d5be6b55a5accfc7fe4c08eab6a8de1fca4855", VALID_PASSWORD);

            RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(changePasswordRequestDto);

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        public void patch_passwordChange_responseIsBadRequest_IfNewPasswordLengthIsNot64() {

            ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(user.getPassword(), INVALID_PASSWORD);

            RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(changePasswordRequestDto);

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        public void patch_changePassword_responseIsUnauthorized_IfAccessTokenNotGiven() {

            ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(user.getPassword(), VALID_PASSWORD);

            RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(changePasswordRequestDto);

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }

        @Test
        public void patch_changePassword_responseNoContent_IfUserIdIsInvalid() {
            String accessTokenWithInvalidUserId = JwtTokenUtil.generateAccessToken(0, UserRole.USER);

            ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(user.getPassword(), VALID_PASSWORD);

            RequestEntity<ChangePasswordRequestDto> request = RequestEntity.patch(URI.create("/v3/users/change-password"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessTokenWithInvalidUserId)
                    .body(changePasswordRequestDto);

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Warehouse API tests.")
    public class WarehouseApiTest {
        private String accessToken = null;
        private Users user = null;

        @BeforeEach
        public void beforeTest() {
            if(user == null) {
                user = Users.builder().name("TEST_NAME")
                        .email("TEST_EMAIL1")
                        .password("123")
                        .type(UserType.OWNER)
                        .phoneNumber("010123123")
                        .telephoneNumber("010123123")
                        .companyName("companyName")
                        .role(UserRole.USER)
                        .build();
                usersRepository.save(user);

                accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
            }
            warehousesRepository.deleteAll();
        }

        @AfterEach
        public void afterTest() {
            if(user != null) {
                user = usersRepository.findByEmail("TEST_EMAIL1").orElseThrow(UserEmailNotFoundException::new);
                usersRepository.delete(user);
            }
            warehousesRepository.deleteAll();
        }

        @Test
        public void delete_warehouse_responseIsOk_IfAllConditionsAreRight() {
            Warehouses warehouse = saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[] { MainItemType.CLOTH });
            String url = "/v3/warehouses/"+warehouse.getId();

            RequestEntity<Void> request = RequestEntity.delete(URI.create(url))
                    .header("Authorization", "Bearer "+accessToken)
                    .build();

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getMessage());

            warehousesRepository.delete(warehouse);
        }

        @Test
        public void delete_warehouse_responseIsUnAuthorized_IfAuthorizationIsEmpty() {
            RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/99999"))
                    .build();

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNotNull(response.getBody().getMessage());
        }

        @Test
        public void delete_warehouse_responseIsNoContent_IfWarehouseNotExist() {
            RequestEntity<Void> request = RequestEntity.delete(URI.create("/v3/warehouses/0"))
                    .header("Authorization", "Bearer "+accessToken)
                    .build();

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_warehouseByAddress_responseIsOk_IfAllConditionsAreRight() {
            Warehouses tempWarehouse = saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[] { MainItemType.CLOTH });

            String addressQuery = "addr";
            String url = String.format("/v3/warehouses?address=%s&page=0&size=4", addressQuery);
            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

            List<WarehouseSearchDto> warehouses = response.getBody().getWarehouses();
            assertTrue(warehouses.size() > 0);

            WarehouseSearchDto warehouse = warehouses.get(0);

            assertEquals(HttpStatus.OK, response.getStatusCode());

            assertNotNull(warehouse.getAddress());
            assertNotNull(warehouse.getWarehouseId());
            assertNotNull(warehouse.getWarehouseCondition());
            assertNotNull(warehouse.getMinReleasePerMonth());
            assertNotNull(warehouse.getName());
            assertNotNull(warehouse.getWarehouseType());
            assertNotNull(warehouse.getCloseAt());
            assertNotNull(warehouse.getMainImageUrl());
            assertNotNull(warehouse.getOpenAt());
            assertNotNull(warehouse.getSpace());
            assertNotNull(warehouse.getDeliveryTypes());
            assertNotNull(warehouse.getMainItemTypes());

            for(WarehouseSearchDto _warehouse : warehouses) {
                String address = _warehouse.getAddress().toLowerCase();
                assertTrue(address.contains(addressQuery.toLowerCase()));
            }

            warehousesRepository.delete(tempWarehouse);
        }

        @Test
        public void get_warehouseByAddress_responseIsNoContent_IfIsViewableIsFalse() {
            Warehouses tempWarehouse = saveWarehouse(WarehouseStatus.IN_PROGRESS, new MainItemType[] { MainItemType.CLOTH });

            String addressQuery = "addr";
            String url = String.format("/v3/warehouses?address=%s&page=0&size=4", addressQuery);
            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            warehousesRepository.delete(tempWarehouse);
        }

        @Test
        public void get_warehouseByAddress_responseIsNoContent_IfWarehouseNotExist() {
            warehousesRepository.deleteAll();
            String addressQuery = "addr";
            String url = String.format("/v3/warehouses?address=%s&page=0&size=4", addressQuery);
            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_warehouseForMain_responseIsOk_IfAllConditionsAreRight() {
            Warehouses tempWarehouse = saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[] { MainItemType.CLOTH });
            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

            List<WarehouseSearchDto> warehouses = response.getBody().getWarehouses();
            assertTrue(warehouses.size() > 0);

            WarehouseSearchDto warehouse = warehouses.get(0);
            assertEquals(HttpStatus.OK, response.getStatusCode());


            assertNotNull(warehouse.getAddress());
            assertNotNull(warehouse.getWarehouseId());
            assertNotNull(warehouse.getWarehouseCondition());
            assertNotNull(warehouse.getMinReleasePerMonth());
            assertNotNull(warehouse.getName());
            assertNotNull(warehouse.getWarehouseType());
            assertNotNull(warehouse.getCloseAt());
            assertNotNull(warehouse.getMainImageUrl());
            assertNotNull(warehouse.getOpenAt());
            assertNotNull(warehouse.getSpace());
            assertNotNull(warehouse.getDeliveryTypes());
            assertNotNull(warehouse.getMainItemTypes());

            warehousesRepository.delete(tempWarehouse);
        }

        @Test
        public void get_warehouseForMain_responseIsNoContent_IfIsViewableIsFalse() {
            Warehouses tempWarehouse = saveWarehouse(WarehouseStatus.IN_PROGRESS, new MainItemType[] { MainItemType.CLOTH });
            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

            warehousesRepository.delete(tempWarehouse);
        }

        @Test
        public void get_warehouseForMain_responseIsNoContent_IfWarehouseNotExist() {
            warehousesRepository.deleteAll();
            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/warehouses?page=0&size=4"))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_warehouseByMainItemType_responseIsOk_IfAllConditionsAreRight() {
            Warehouses warehouse1 = saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[] { MainItemType.CLOTH, MainItemType.COSMETIC });
            Warehouses warehouse2 = saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[] { MainItemType.CLOTH, MainItemType.ACCESSORY });
            Warehouses warehouse3 = saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[] { MainItemType.CLOTH, MainItemType.BOOK });

            String url = "/v3/warehouses?page=0&size=5&mainItemTypes=CLOTH,COSMETIC";

            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

            List<WarehouseSearchDto> warehouses = response.getBody().getWarehouses();
            assertTrue(warehouses.size() > 0);

            WarehouseSearchDto warehouseSearchDto = warehouses.get(0);

            assertEquals(HttpStatus.OK, response.getStatusCode());

            assertNotNull(warehouseSearchDto.getAddress());
            assertNotNull(warehouseSearchDto.getWarehouseId());
            assertNotNull(warehouseSearchDto.getWarehouseCondition());
            assertNotNull(warehouseSearchDto.getMinReleasePerMonth());
            assertNotNull(warehouseSearchDto.getName());
            assertNotNull(warehouseSearchDto.getWarehouseType());
            assertNotNull(warehouseSearchDto.getCloseAt());
            assertNotNull(warehouseSearchDto.getMainImageUrl());
            assertNotNull(warehouseSearchDto.getOpenAt());
            assertNotNull(warehouseSearchDto.getSpace());
            assertNotNull(warehouseSearchDto.getDeliveryTypes());
            assertNotNull(warehouseSearchDto.getMainItemTypes());

            for (WarehouseSearchDto _warehouse : warehouses) {
                _warehouse.getMainItemTypes().stream()
                        .filter(mainItemTypeMatchDto -> mainItemTypeMatchDto.getName() == MainItemType.CLOTH || mainItemTypeMatchDto.getName() == MainItemType.COSMETIC)
                        .forEach(mainItemTypeMatchDto -> assertTrue(mainItemTypeMatchDto.getMatch()));

                _warehouse.getMainItemTypes().stream()
                        .filter(mainItemTypeMatchDto -> mainItemTypeMatchDto.getName() != MainItemType.CLOTH && mainItemTypeMatchDto.getName() != MainItemType.COSMETIC)
                        .forEach(mainItemTypeMatchDto -> assertFalse(mainItemTypeMatchDto.getMatch()));
            }

            warehousesRepository.delete(warehouse1);
            warehousesRepository.delete(warehouse2);
            warehousesRepository.delete(warehouse3);
        }

        @Test
        public void get_warehouseByMainItemType_responseIsNoContent_IfIsViewableIsFalse() {
            Warehouses warehouse = saveWarehouse(WarehouseStatus.IN_PROGRESS, new MainItemType[] { MainItemType.CLOTH });

            String mainItemType = MainItemType.CLOTH.toString();
            String url = String.format("/v3/warehouses?mainItemTypes=%s&page=0&size=5", mainItemType);

            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            warehousesRepository.delete(warehouse);
        }

        @Test
        public void get_warehouseByMainItemType_responseIsNoContent_IfWarehouseNotExist() {
            warehousesRepository.deleteAll();

            String mainItemType = MainItemType.CLOTH.toString();
            String url = String.format("/v3/warehouses?mainItemTypes=%s&page=0&size=5", mainItemType);

            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_warehouse_responseIsBadRequest_IfAddressAndMainItemTypeBothGiven() {
            warehousesRepository.deleteAll();

            String mainItemType = MainItemType.CLOTH.toString();
            String addressQuery = "addr";
            String url = String.format("/v3/warehouses?mainItemTypes=%s&address=%s&page=0&offset=5", mainItemType, addressQuery);

            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .build();

            ResponseEntity<WarehouseSearchResponseDto> response = restTemplate.exchange(request, WarehouseSearchResponseDto.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        public void get_warehouseDetail_responseIsOk_IfAllConditionsAreRight() {
            Warehouses _warehouse = saveWarehouse(WarehouseStatus.VIEWABLE, new MainItemType[] { MainItemType.CLOTH });
            String url = String.format("/v3/warehouses/%d", _warehouse.getId());

            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

            WarehouseDetailResponseDto warehouse = response.getBody();
            assertEquals(HttpStatus.OK, response.getStatusCode());

            assertNotNull(warehouse.getWarehouseId());
            assertNotNull(warehouse.getOwnerId());
            assertNotNull(warehouse.getName());
            assertNotNull(warehouse.getSpace());
            assertNotNull(warehouse.getAddress());
            assertNotNull(warehouse.getAddressDetail());
            assertNotNull(warehouse.getDescription());
            assertNotNull(warehouse.getAvailableWeekdays());
            assertNotNull(warehouse.getOpenAt());
            assertNotNull(warehouse.getCloseAt());
            assertNotNull(warehouse.getAvailableTimeDetail());
            assertNotNull(warehouse.getCctvExist());
            assertNotNull(warehouse.getDoorLockExist());
            assertNotNull(warehouse.getAirConditioningType());
            assertNotNull(warehouse.getWorkerExist());
            assertNotNull(warehouse.getCanPark());
            assertNotNull(warehouse.getMainItemTypes());
            assertNotNull(warehouse.getWarehouseType());
            assertNotNull(warehouse.getMinReleasePerMonth());
            assertNotNull(warehouse.getLatitude());
            assertNotNull(warehouse.getLongitude());
            assertNotNull(warehouse.getMainImageUrl());
            assertNotNull(warehouse.getDeliveryTypes());
            assertNotNull(warehouse.getWarehouseCondition());
            assertNotNull(warehouse.getWarehouseFacilityUsages());
            assertNotNull(warehouse.getWarehouseUsageCautions());
            assertNotNull(warehouse.getImages());

            warehousesRepository.delete(_warehouse);
        }

        @Test
        public void get_warehouseDetail_responseIsForbidden_IfIsViewableIsFalse() {
            Warehouses _warehouse = saveWarehouse(WarehouseStatus.IN_PROGRESS, new MainItemType[]{MainItemType.CLOTH});
            String url = String.format("/v3/warehouses/%d", _warehouse.getId());

            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            warehousesRepository.delete(_warehouse);
        }

        @Test
        public void get_warehouseDetail_responseIsNoContent_IfWarehouseNotExist() {
            String url = String.format("/v3/warehouses/%d", 0);

            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            ResponseEntity<WarehouseDetailResponseDto> response = restTemplate.exchange(request, WarehouseDetailResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        private Warehouses saveWarehouse(WarehouseStatus status, MainItemType[] mainItemTypes) {
            int userId = JwtTokenUtil.extractUserId(accessToken);

            Warehouses warehouse = Warehouses.builder()
                    .userId(userId)
                    .name("NAME")
                    .space(123)
                    .address("address")
                    .addressDetail("addressDetail")
                    .description("description")
                    .availableWeekdays(1)
                    .openAt("06:00")
                    .closeAt("18:00")
                    .availableTimeDetail("availableTimeDetail")
                    .cctvExist(true)
                    .doorLockExist(true)
                    .airConditioningType(AirConditioningType.HEATING)
                    .workerExist(true)
                    .canPark(true)
                    .warehouseType(WarehouseType.THREEPL)
                    .minReleasePerMonth(2)
                    .latitude(22.2)
                    .longitude(22.2)
                    .status(status)
                    .build();

            List<MainItemTypes> m = Arrays.stream(mainItemTypes)
                    .map(mainItemType -> new MainItemTypes(mainItemType, warehouse))
                    .collect(Collectors.toList());

            warehouse.getMainItemTypes().addAll(m);

            return warehousesRepository.save(warehouse);
        }
    }

    @Nested
    @DisplayName("Admin API tests.")
    public class AdminApiTest {

        private Users user = null;
        private String accessToken = null;
        private Users admin = null;
        private String adminAccessToken = null;

        @BeforeEach
        public void beforeTest() {
            if(user == null) {
                user = userEntityFactory.createUser();
                accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), UserRole.USER);
            }
            if(admin == null) {
                admin = userEntityFactory.createAdmin();
                adminAccessToken = JwtTokenUtil.generateAccessToken(admin.getUserId(), UserRole.ADMIN);
            }
            warehousesRepository.deleteAll();
        }

        @AfterEach
        public void afterTest() {
            warehousesRepository.deleteAll();
            usersRepository.deleteAll();
        }

        @Test
        public void get_InProgressWarehouses_ResultIsNoContent_ifNotExist() {
            String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());
            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .build();

            warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
            warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.COSMETIC, MainItemType.CLOTH});
            warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.ELECTRONICS, MainItemType.SPORTS});

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_InProgressWarehouses_ResultHasItems_ifExist() {
            String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());

            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .build();

            warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD});
            warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.COSMETIC, MainItemType.CLOTH});
            warehouseEntityFactory.createInProgressWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.ELECTRONICS, MainItemType.SPORTS});
            warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.GENERAL_MERCHANDISE, MainItemType.BOOK});

            ResponseEntity<WarehouseInsertRequestResponseListDto> response = restTemplate.exchange(request, WarehouseInsertRequestResponseListDto.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().getRequests().size() > 0);
            assertNotNull(response.getBody().getRequests().get(0).getName());
            assertNotNull(response.getBody().getRequests().get(0).getWarehouseId());
            assertNotNull(response.getBody().getRequests().get(0).getCreatedAt());
        }

        @Test
        public void get_InProgressWarehouses_responseIsForbidden_IfTokenIsBad() {
            String url = String.format("/v3/admin/warehouses?page=%d&size=%d&status=%s", 0, 4, WarehouseStatus.IN_PROGRESS.name());
            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        public void get_AllInfosOfSpecificWarehouse_responseIsOk_ifAllConditionsAreRight() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
            Integer warehouseId = warehouse.getId();
            String url = String.format("/v3/admin/warehouses/%d",warehouseId);
            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .build();
            ResponseEntity<WarehouseAdminDetailResponseDto> response = restTemplate.exchange(request, WarehouseAdminDetailResponseDto.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(WarehouseEntityFactory.NAME, response.getBody().getName());
            assertEquals(WarehouseEntityFactory.SPACE, response.getBody().getSpace());
            assertEquals(WarehouseEntityFactory.ADDRESS, response.getBody().getAddress());
            assertEquals(WarehouseEntityFactory.ADDRESS_DETAIL, response.getBody().getAddressDetail());
            assertEquals(WarehouseEntityFactory.DESCRIPTION, response.getBody().getDescription());
            assertEquals(WarehouseEntityFactory.AVAILABLE_WEEKDAYS, response.getBody().getAvailableWeekdays());
            assertEquals(WarehouseEntityFactory.OPEN_AT, response.getBody().getOpenAt());
            assertEquals(WarehouseEntityFactory.CLOSE_AT, response.getBody().getCloseAt());
            assertEquals(WarehouseEntityFactory.AVAILABLE_TIME_DETAIL, response.getBody().getAvailableTimeDetail());
            assertEquals(WarehouseEntityFactory.CCTV_EXISTS, response.getBody().getCctvExist());
            assertEquals(WarehouseEntityFactory.DOOR_LOCK_EXIST, response.getBody().getDoorLockExist());
            assertEquals(WarehouseEntityFactory.AIR_CONDITIONING_TYPE, response.getBody().getAirConditioningType());
            assertEquals(WarehouseEntityFactory.WORKER_EXIST, response.getBody().getWorkerExist());
            assertEquals(WarehouseEntityFactory.CAN_PARK, response.getBody().getCanPark());
            assertEquals(WarehouseEntityFactory.WAREHOUSE_TYPE, response.getBody().getWarehouseType());
            assertEquals(WarehouseEntityFactory.MIN_RELEASE_PER_MONTH, response.getBody().getMinReleasePerMonth());
            assertEquals(WarehouseEntityFactory.LATITUDE, response.getBody().getLatitude());
            assertEquals(WarehouseEntityFactory.LONGITUDE, response.getBody().getLongitude());
            assertEquals(response.getBody().getDeliveryTypes(), Arrays.asList(WarehouseEntityFactory.DELIVERY_TYPES));
            assertEquals(response.getBody().getInsurances(), Arrays.asList(WarehouseEntityFactory.INSURANCES));
            assertEquals(response.getBody().getSecurityCompanies(), Arrays.asList(WarehouseEntityFactory.SECURITY_COMPANIES));
            assertNotNull(response.getBody().getCreatedAt());
            assertEquals(response.getBody().getWarehouseCondition(), Arrays.asList(WarehouseEntityFactory.WAREHOUSE_CONDITIONS));
            assertEquals(WarehouseStatus.VIEWABLE, response.getBody().getStatus());
        }

        @Test
        public void get_AllInfosOfSpecificWarehouse_responseIsNoContent_IfWarehouseNotExist() {
            String url = String.format("/v3/admin/warehouses/%d", 0);
            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .build();
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_AllInfosOfSpecificWarehouse_responseIsForbidden_IfTokenIsWrong() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
            Integer warehouseId = warehouse.getId();
            String url = String.format("/v3/admin/warehouses/%d", warehouseId);
            RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        public void put_WarehouseInfoIsUpdated_ifAllConditionsAreRight() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithMainItemTypes(accessToken, new MainItemType[]{MainItemType.BOOK, MainItemType.FOOD, MainItemType.CLOTH});
            Integer warehouseId = warehouse.getId();
            String url = String.format("/v3/admin/warehouses/%d", warehouseId);
            WarehouseAdminUpdateRequestDto body = WarehouseAdminUpdateRequestDto.builder()
                    .name("NEW NAME")
                    .space(999)
                    .address("NEW ADDRESS")
                    .addressDetail("NEW ADDR_DETAIL")
                    .description("NEW DESC")
                    .availableWeekdays(101010)
                    .openAt("08:00")
                    .closeAt("23:30")
                    .availableTimeDetail("NEW AVAIL_TIME_DETAIL")
                    .cctvExist(false)
                    .doorLockExist(false)
                    .airConditioningType(AirConditioningType.NONE)
                    .workerExist(false)
                    .canPark(false)
                    .mainItemTypes(Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}))
                    .warehouseType(WarehouseType.FULFILLMENT)
                    .warehouseCondition(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}))
                    .minReleasePerMonth(101)
                    .latitude(11.11)
                    .longitude(33.33)
                    .insurances(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}))
                    .securityCompanies(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}))
                    .deliveryTypes(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}))
                    .status(WarehouseStatus.REJECTED)
                    .warehouseFacilityUsages(Arrays.asList(new String[]{"WH_FACILITY_USAGE"}))
                    .build();
            RequestEntity<WarehouseAdminUpdateRequestDto> putRequest = RequestEntity.put(URI.create(url))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .body(body);

            ResponseEntity<WarehouseAdminDetailResponseDto> firstResponse = restTemplate.exchange(putRequest, WarehouseAdminDetailResponseDto.class);
            assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
            assertEquals("NEW NAME", firstResponse.getBody().getName());
            assertEquals(Integer.valueOf(999), firstResponse.getBody().getSpace());
            assertEquals("NEW ADDRESS", firstResponse.getBody().getAddress());
            assertEquals("NEW ADDR_DETAIL", firstResponse.getBody().getAddressDetail());
            assertEquals("NEW DESC", firstResponse.getBody().getDescription());
            assertEquals(Integer.valueOf(101010), firstResponse.getBody().getAvailableWeekdays());
            assertEquals("08:00", firstResponse.getBody().getOpenAt());
            assertEquals("23:30", firstResponse.getBody().getCloseAt());
            assertEquals("NEW AVAIL_TIME_DETAIL", firstResponse.getBody().getAvailableTimeDetail());
            assertFalse(firstResponse.getBody().getCctvExist());
            assertFalse(firstResponse.getBody().getDoorLockExist());
            assertFalse(firstResponse.getBody().getWorkerExist());
            assertFalse(firstResponse.getBody().getCanPark());
            assertEquals(AirConditioningType.NONE, firstResponse.getBody().getAirConditioningType());
            assertEquals(firstResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
            assertEquals(WarehouseType.FULFILLMENT, firstResponse.getBody().getWarehouseType());
            assertEquals(Integer.valueOf(101), firstResponse.getBody().getMinReleasePerMonth());
            assertEquals(Double.valueOf(11.11), firstResponse.getBody().getLatitude());
            assertEquals(Double.valueOf(33.33), firstResponse.getBody().getLongitude());
            assertTrue(firstResponse.getBody().getInsurances().containsAll(Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"})));
            assertTrue(firstResponse.getBody().getSecurityCompanies().containsAll(Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"})));
            assertTrue(firstResponse.getBody().getDeliveryTypes().containsAll(Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"})));
            assertTrue(firstResponse.getBody().getWarehouseCondition().containsAll(Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS})));
            assertEquals(WarehouseStatus.REJECTED, firstResponse.getBody().getStatus());
            assertTrue(firstResponse.getBody().getWarehouseFacilityUsages().contains("WH_FACILITY_USAGE"));

            RequestEntity<Void> getRequest = RequestEntity.get(URI.create(url)).
                    header("Authorization", "Bearer " + adminAccessToken).build();
            ResponseEntity<WarehouseAdminDetailResponseDto> secondResponse = restTemplate.exchange(getRequest, WarehouseAdminDetailResponseDto.class);
            assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
            assertEquals("NEW NAME", secondResponse.getBody().getName());
            assertEquals(Integer.valueOf(999), secondResponse.getBody().getSpace());
            assertEquals("NEW ADDRESS", secondResponse.getBody().getAddress());
            assertEquals("NEW ADDR_DETAIL", secondResponse.getBody().getAddressDetail());
            assertEquals("NEW DESC", secondResponse.getBody().getDescription());
            assertEquals(Integer.valueOf(101010), secondResponse.getBody().getAvailableWeekdays());
            assertEquals("08:00", secondResponse.getBody().getOpenAt());
            assertEquals("23:30", secondResponse.getBody().getCloseAt());
            assertEquals("NEW AVAIL_TIME_DETAIL", secondResponse.getBody().getAvailableTimeDetail());
            assertFalse(secondResponse.getBody().getCctvExist());
            assertFalse(secondResponse.getBody().getDoorLockExist());
            assertFalse(secondResponse.getBody().getWorkerExist());
            assertFalse(secondResponse.getBody().getCanPark());
            assertEquals(AirConditioningType.NONE, secondResponse.getBody().getAirConditioningType());
            assertEquals(secondResponse.getBody().getMainItemTypes(), Arrays.asList(new MainItemType[]{MainItemType.COSMETIC, MainItemType.COLD_STORAGE, MainItemType.ELECTRONICS}));
            assertEquals(WarehouseType.FULFILLMENT, secondResponse.getBody().getWarehouseType());
            assertEquals(Integer.valueOf(101), secondResponse.getBody().getMinReleasePerMonth());
            assertEquals(Double.valueOf(11.11), secondResponse.getBody().getLatitude());
            assertEquals(Double.valueOf(33.33), secondResponse.getBody().getLongitude());
            assertEquals(secondResponse.getBody().getInsurances(), Arrays.asList(new String[]{"NEW_INSURANCE_1", "NEW_INSURANCE_2"}));
            assertEquals(secondResponse.getBody().getSecurityCompanies(), Arrays.asList(new String[]{"NEW_SEC_COMP_1", "NEW_SEC_COMP_2"}));
            assertEquals(secondResponse.getBody().getDeliveryTypes(), Arrays.asList(new String[]{"NEW_DELIVERY_1", "NEW_DELIVERY_2"}));
            assertEquals(secondResponse.getBody().getWarehouseCondition(), Arrays.asList(new WarehouseCondition[]{WarehouseCondition.BONDED, WarehouseCondition.HAZARDOUS}));
            assertEquals(secondResponse.getBody().getWarehouseFacilityUsages(), Arrays.asList(new String[]{"WH_FACILITY_USAGE"}));
            assertEquals(WarehouseStatus.REJECTED, secondResponse.getBody().getStatus());
        }

        @Test
        public void get_adminAllEstimates_responseIsOk_IfAllConditionsAreRight() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getEstimates());

            response.getBody().getEstimates().stream()
                    .forEach(estimateSearchDto -> {
                        assertNotNull(estimateSearchDto.getId());
                        assertNotNull(estimateSearchDto.getWarehouse());
                        assertEquals(estimateSearchDto.getWarehouse().getWarehouseId(), warehouse.getId());
                        assertEquals(estimateSearchDto.getWarehouse().getAddress(), warehouse.getAddress());
                        assertEquals(estimateSearchDto.getWarehouse().getName(), warehouse.getName());
                        assertNotNull(estimateSearchDto.getStatus());
                    });
        }

        @Test
        public void get_adminReceptedEstimates_responseIsOk_IfAllConditionsAreRight() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10&status=RECEPTED"))
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getEstimates());

            response.getBody().getEstimates().stream()
                    .forEach(estimateSearchDto -> {
                        assertNotNull(estimateSearchDto.getId());
                        assertNotNull(estimateSearchDto.getWarehouse());
                        assertEquals(estimateSearchDto.getWarehouse().getWarehouseId(), warehouse.getId());
                        assertEquals(estimateSearchDto.getWarehouse().getAddress(), warehouse.getAddress());
                        assertEquals(estimateSearchDto.getWarehouse().getName(), warehouse.getName());
                        assertEquals(estimateSearchDto.getStatus(), EstimateStatus.RECEPTED);
                    });
        }

        @Test
        public void get_adminInProgressEstimates_responseIsOk_IfAllConditionsAreRight() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10&status=IN_PROGRESS"))
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getEstimates());

            response.getBody().getEstimates().stream()
                    .forEach(estimateSearchDto -> {
                        assertNotNull(estimateSearchDto.getId());
                        assertNotNull(estimateSearchDto.getWarehouse());
                        assertEquals(estimateSearchDto.getWarehouse().getWarehouseId(), warehouse.getId());
                        assertEquals(estimateSearchDto.getWarehouse().getAddress(), warehouse.getAddress());
                        assertEquals(estimateSearchDto.getWarehouse().getName(), warehouse.getName());
                        assertEquals(estimateSearchDto.getStatus(), EstimateStatus.IN_PROGRESS);
                    });
        }

        @Test
        public void get_adminDoneEstimates_responseIsOk_IfAllConditionsAreRight() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
            estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10&status=DONE"))
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getEstimates());

            response.getBody().getEstimates().stream()
                    .forEach(estimateSearchDto -> {
                        assertNotNull(estimateSearchDto.getId());
                        assertNotNull(estimateSearchDto.getWarehouse());
                        assertEquals(estimateSearchDto.getWarehouse().getWarehouseId(), warehouse.getId());
                        assertEquals(estimateSearchDto.getWarehouse().getAddress(), warehouse.getAddress());
                        assertEquals(estimateSearchDto.getWarehouse().getName(), warehouse.getName());
                        assertEquals(estimateSearchDto.getStatus(), EstimateStatus.DONE);
                    });
        }

        @Test
        public void get_adminAllEstimates_responseIsNoContent_IfEstimatesNotExist() {
            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
                    .header("Authorization", "Bearer " + adminAccessToken)
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_adminAllEstimates_responseIsUnAuthorized_IfAccessTokenNotGiven() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }

        @Test
        public void get_adminAllEstimates_responseIsForbidden_IfNotAdmin() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            Estimates estimate1 = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate2 = estimateEntityFactory.createDoneWithEstimateItems(warehouse.getId(), user.getUserId());
            Estimates estimate3 = estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/admin/estimates?page=0&size=10"))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Estimate API tests.")
    public class EstimateApiTests {
        private Users user = null;
        private String accessToken = null;

        @BeforeEach
        public void beforeTest() {
            estimatesRepository.deleteAll();
            usersRepository.deleteAll();
            warehousesRepository.deleteAll();

            user = userEntityFactory.createUser();
            accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
        }

        @AfterEach
        public void afterTest() {
            estimatesRepository.deleteAll();
            usersRepository.deleteAll();
            warehousesRepository.deleteAll();
        }

        @Test
        public void post_estimate_responseIsOk_IfAllConditionsAreRight() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

            EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

            RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(newEstimateInsertRequestDto);

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getMessage());

            warehousesRepository.delete(warehouse);
        }

        @Test
        public void post_estimate_responseIsUnauthorized_IfAccessTokenNotGiven() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);

            EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

            RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(newEstimateInsertRequestDto);

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNotNull(response.getBody().getMessage());

            warehousesRepository.delete(warehouse);
        }

        @Test
        public void post_estimate_responseIsForbidden_IfWarehouseNotViewable() {
            Warehouses warehouse = warehouseEntityFactory.createInProgressWithNoMainItemTypes(accessToken);

            EstimateInsertRequestDto newEstimateInsertRequestDto = EstimatesInsertRequestFactory.create(warehouse.getId());

            RequestEntity<EstimateInsertRequestDto> request = RequestEntity.post(URI.create("/v3/estimates"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(newEstimateInsertRequestDto);

            ResponseEntity<BasicMessageResponseDto> response = restTemplate.exchange(request, BasicMessageResponseDto.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNotNull(response.getBody().getMessage());

            warehousesRepository.delete(warehouse);
        }

        @Test
        public void get_estimateByUserId_responseIsOk_IfAllConditionsAreRight() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/estimates"))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getEstimates());

            response.getBody().getEstimates().stream()
                    .forEach(estimateSearchDto -> {
                        assertNotNull(estimateSearchDto.getId());
                        assertNotNull(estimateSearchDto.getWarehouse());
                        assertEquals(estimateSearchDto.getWarehouse().getWarehouseId(), warehouse.getId());
                        assertEquals(estimateSearchDto.getWarehouse().getAddress(), warehouse.getAddress());
                        assertEquals(estimateSearchDto.getWarehouse().getName(), warehouse.getName());
                        assertEquals(estimateSearchDto.getStatus(), EstimateStatus.RECEPTED);
                    });
        }

        @Test
        public void get_estimateByUserId_responseIsNoContent_IfEstimatesNotExist() {
            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/estimates"))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_estimateByUserId_responseIsUnAuthorized_IfAccessTokenNotGiven() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/estimates"))
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }


        @Test
        public void get_estimateByUserId_responseIsForbidden_IfOtherUserId() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());
            estimateEntityFactory.createReceptedWithEstimateItems(warehouse.getId(), user.getUserId());

            String otherUsersAccessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER);

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/users/"+user.getUserId()+"/estimates"))
                    .header("Authorization", "Bearer " + otherUsersAccessToken)
                    .build();

            ResponseEntity<EstimateSearchResponseDto> response = restTemplate.exchange(request, EstimateSearchResponseDto.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Estimate Items API tests.")
    public class EstimateItemsApiTest {
        private String accessToken = null;
        private Users user = null;

        @BeforeEach
        public void beforeTest() {
            estimatesRepository.deleteAll();
            usersRepository.deleteAll();
            warehousesRepository.deleteAll();

            user = userEntityFactory.createUser();
            accessToken = JwtTokenUtil.generateAccessToken(user.getUserId(), user.getRole());
        }

        @AfterEach
        public void afterTest() {
            estimatesRepository.deleteAll();
            usersRepository.deleteAll();
            warehousesRepository.deleteAll();
        }

        @Test
        public void get_estimateItemsByEstimateId_responseIsOk_IfAllConditionsAreRight() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getEstimateItems());

            response.getBody().getEstimateItems().stream()
                    .forEach(estimateSearchDto -> {
                        assertNotNull(estimateSearchDto.getId());
                        assertEquals(estimateSearchDto.getName(), EstimateItemEntityFactory.NAME);
                        assertEquals(estimateSearchDto.getKeepingNumber(), EstimateItemEntityFactory.KEEPING_NUMBER);
                        assertEquals(estimateSearchDto.getWeight(), EstimateItemEntityFactory.WEIGHT);
                        assertEquals(estimateSearchDto.getBarcode(), EstimateItemEntityFactory.BARCODE);
                        assertEquals(estimateSearchDto.getSku(), EstimateItemEntityFactory.SKU);
                        assertEquals(estimateSearchDto.getUrl(), EstimateItemEntityFactory.URL);
                        assertEquals(estimateSearchDto.getPerimeter(), EstimateItemEntityFactory.PERIMETER);
                        assertEquals(estimateSearchDto.getKeepingType(), EstimateItemEntityFactory.KEEPING_TYPE);
                    });
        }

        @Test
        public void get_estimateItemsByestimateId_responseIsNoContent_IfEstimateNotExist() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            final int estimateId = 0;

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimateId+"/items"))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_estimateItemsByestimateId_responseIsNoContent_IfEstimateItemsNotExist() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            Estimates estimate = estimateEntityFactory.createInProgressWithoutEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void get_estimateItemsByestimateId_responseIsUnAuthorized_IfAccessTokenNotGiven() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                    .build();

            ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }

        @Test
        public void get_estimateItemsByestimateId_responseIsForbidden_IfOtherUsersAccessTokenIsGiven() {
            Warehouses warehouse = warehouseEntityFactory.createViewableWithNoMainItemTypes(accessToken);
            Estimates estimate = estimateEntityFactory.createInProgressWithEstimateItems(warehouse.getId(), user.getUserId());
            String otherUsersAccessToken = JwtTokenUtil.generateAccessToken(0, UserRole.USER);

            RequestEntity<Void> request = RequestEntity.get(URI.create("/v3/estimates/"+estimate.getId()+"/items"))
                    .header("Authorization", "Bearer " + otherUsersAccessToken)
                    .build();

            ResponseEntity<EstimateItemSearchResponseDto> response = restTemplate.exchange(request, EstimateItemSearchResponseDto.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Health check API test.")
    public class HealthCheckTest {
        @Test
        public void healthCheckIsOK() {
            RequestEntity<Void> request = RequestEntity.get(URI.create("/health-check")).build();
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            assertTrue(response.getBody().equalsIgnoreCase("I AM HEALTHY!"));
        }
    }
}
