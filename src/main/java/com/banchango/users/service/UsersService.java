package com.banchango.users.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.exception.InternalServerErrorException;
import com.banchango.common.service.EmailSender;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.tools.Email;
import com.banchango.tools.EmailContent;
import com.banchango.tools.PasswordGenerator;
import com.banchango.users.dto.UserInfoResponseDto;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSigninResponseDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final EmailSender emailSender;

    @Value("${banchango.email.id}")
    private String senderEmail;

    @Value("${banchango.email.password}")
    private String senderEmailPassword;

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
        UserInfoResponseDto userInfoDto = new UserInfoResponseDto(usersRepository.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword()).orElseThrow(UserNotFoundException::new));
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
    public UserInfoResponseDto updateUserInfo(UserSignupRequestDto requestDto, Integer userId, String token) {
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
        return emailSender.send(user.getEmail(), emailContent);
    }

    public BasicMessageResponseDto sendTestEmail(String token) {
        Integer userId = JwtTokenUtil.extractUserId(token);
        Users user = usersRepository.findById(userId).orElseThrow(UserIdNotFoundException::new);
        EmailContent emailContent = new EmailContent("[반창고] 창고 등록 요청 안내", "안녕하세요, 반창고 입니다!", "상우 로지스에 대한 창고 등록 요청이 완료되었으며, 영업 팀의 인증 절차 후 등록이 완료될 예정입니다.", "문의사항은 wherehousegm@gmail.com으로 답변 주세요.", "반창고", "dev.banchango.shop");
        return emailSender.send(user.getEmail(), emailContent);
    }
}