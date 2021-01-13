package com.banchango.users.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.service.EmailSender;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.domain.withdraws.Withdraws;
import com.banchango.domain.withdraws.WithdrawsRepository;
import com.banchango.tools.EmailContent;
import com.banchango.tools.PasswordGenerator;
import com.banchango.users.dto.*;
import com.banchango.users.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final EmailSender emailSender;
    private final WithdrawsRepository withdrawsRespository;
    private final WarehousesRepository warehousesRepository;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(Integer userId, String token) {
        if(!userId.equals(JwtTokenUtil.extractUserId(token))) {
            throw new UserInvalidAccessException();
        }
        return new UserInfoResponseDto(usersRepository.findById(userId).orElseThrow(UserIdNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public UserSigninResponseDto signIn(UserSigninRequestDto requestDto) {
        UserSigninResponseDto responseDto = new UserSigninResponseDto();
        Users user = usersRepository.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword()).orElseThrow(UserNotFoundException::new);

        Optional<Withdraws> optionalWithdraw = withdrawsRespository.findByUserId(user.getUserId());
        if(optionalWithdraw.isPresent()) throw new UserNotFoundException("탈퇴한 사용자입니다");

        UserInfoResponseDto userInfoDto = new UserInfoResponseDto(user);
        responseDto.setAccessToken(JwtTokenUtil.generateAccessToken(userInfoDto.getUserId(), userInfoDto.getRole()));
        responseDto.setRefreshToken(JwtTokenUtil.generateRefreshToken(userInfoDto.getUserId(), userInfoDto.getRole()));
        responseDto.setTokenType("Bearer");
        responseDto.setUser(userInfoDto);
        return responseDto;
    }

    @Transactional
    public UserInfoResponseDto signUp(UserSignupRequestDto requestDto) {
        if (usersRepository.findByEmail(requestDto.getEmail()).isPresent()) throw new UserEmailInUseException();
        return new UserInfoResponseDto(usersRepository.save(requestDto.toEntity()));
    }

    @Transactional
    public UserInfoResponseDto updateUserInfo(UserUpdateRequestDto requestDto, Integer userId, String token) {
        if(!userId.equals(JwtTokenUtil.extractUserId(token))) {
            throw new UserInvalidAccessException();
        }
        Users user = usersRepository.findById(userId).orElseThrow(UserIdNotFoundException::new);
        user.updateUserInfo(requestDto);
        return new UserInfoResponseDto(user);
    }

    @Transactional
    public BasicMessageResponseDto sendTemporaryPasswordEmail(String recipient) {
        String temporaryPassword = PasswordGenerator.generate();
        Users user = usersRepository.findByEmail(recipient).orElseThrow(UserEmailNotFoundException::new);
        usersRepository.updatePassword(temporaryPassword, recipient);
        EmailContent emailContent = new EmailContent("[반창고] 임시 비밀번호 발급", "안녕하세요, 반창고 입니다!", "발급해드린 임시 비밀번호는 <span style='font-size: 20px'>" + temporaryPassword + "</span> 입니다.", "이 임시 비밀번호로 로그인 해주세요.", "로그인 하기", "dev.banchango.shop/login");
        return emailSender.send(user.getEmail(), emailContent, false);
    }

    @Transactional
    public void changePassword(String accessToken, ChangePasswordRequestDto changePasswordRequestDto) {
        Integer userId = JwtTokenUtil.extractUserId(accessToken);

        Users user = usersRepository.findById(userId).orElseThrow(UserIdNotFoundException::new);

        String originalPasswordFromTable = user.getPassword();
        String originalPasswordFromRequset = changePasswordRequestDto.getOriginalPassword();
        if(!originalPasswordFromRequset.equals(originalPasswordFromTable)) throw new PasswordDoesNotMatchException();

        user.updatePassword(changePasswordRequestDto.getNewPassword());
    }

    @Transactional
    public void withdrawUser(String accessToken, UserWithdrawRequestDto userWithdrawRequestDto) {
        int userId = JwtTokenUtil.extractUserId(accessToken);
        deleteUser(userId, userWithdrawRequestDto);
        deleteWarehousesOwnedByUser(userId);
    }

    private void deleteUser(int userId, UserWithdrawRequestDto userWithdrawRequestDto) {
        Users user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Optional<Withdraws> optionalWithdraws = withdrawsRespository.findByUserId(userId);
        if(optionalWithdraws.isPresent()) throw new UserAlreayWithdrawnException();

        Withdraws withdraw = Withdraws.builder()
            .userId(userId)
            .cause(userWithdrawRequestDto.getCause())
            .build();

        withdrawsRespository.save(withdraw);
    }

    private void deleteWarehousesOwnedByUser(int userId) {
        List<Warehouses> warehouses = warehousesRepository.findByUserId(userId);
        warehouses.stream()
            .forEach(warehouse -> warehouse.updateStatus(WarehouseStatus.DELETED));
    }
}