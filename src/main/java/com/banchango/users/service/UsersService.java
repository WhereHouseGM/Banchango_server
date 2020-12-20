package com.banchango.users.service;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.tools.Email;
import com.banchango.tools.EmailContent;
import com.banchango.tools.ObjectMaker;
import com.banchango.tools.PasswordGenerator;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.json.JSONObject;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Value("${BANCHANGO_EMAIL_ID}")
    private String senderEmail;

    @Value("${BANCHANGO_EMAIL_PASSWORD}")
    private String senderEmailPassword;

    @Transactional
    public JSONObject signUp(UserSignupRequestDto requestDto) throws Exception {
        if(usersRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new UserEmailInUseException();
        }
        Users user = Users.builder().name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .userType(UserType.valueOf(requestDto.getType()))
                .telephoneNumber(requestDto.getTelephoneNumber())
                .companyName(requestDto.getCompanyName())
                .phoneNumber(requestDto.getPhoneNumber())
                .build();
        Users savedUser = usersRepository.save(user);
        return ObjectMaker.getJSONObjectWithUserInfo(savedUser);
    }

    @Transactional(readOnly = true)
    public JSONObject signIn(UserSigninRequestDto requestDto) throws Exception{
        if(requestDto.getEmail() == null || requestDto.getPassword() == null) {
            throw new Exception();
        }
        Optional<Users> user = usersRepository.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
        if(user.isPresent()) {
            JSONObject jsonObject = ObjectMaker.getJSONObjectWithUserInfo(user.get());
            jsonObject.put("accessToken", JwtTokenUtil.generateAccessToken(user.get().getUserId()));
            jsonObject.put("refreshToken", JwtTokenUtil.generateRefreshToken(user.get().getUserId()));
            jsonObject.put("tokenType", "Bearer");
            if(user.get().getEmail().equals("wherehousegm@gmail.com")) {
                jsonObject.put("isAdmin", true);
            }
            return jsonObject;
        }
        else {
            throw new UserNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public JSONObject viewUserInfo(Integer userId, String token) throws Exception{
        if(!JwtTokenUtil.isTokenValidatedWithUserId(JwtTokenUtil.getToken(token), userId)) {
            throw new AuthenticateException();
        }
        Optional<Users> user = usersRepository.findById(userId);
        if(user.isPresent()) {
            return ObjectMaker.getJSONObjectWithUserInfo(user.get());
        }
        else {
            throw new UserIdNotFoundException();
        }
    }

    @Transactional
    public Users updateUserInfo(Integer userId, UserSignupRequestDto requestDto, String accessToken) {

        if(!JwtTokenUtil.isTokenValidated(accessToken)) {
            throw new AuthenticateException("Token is Out of Date");
        }
        if(!JwtTokenUtil.isTokenValidatedWithUserId(accessToken, userId)) {
            throw new UserInvalidAccessException();
        }

        Optional<Users> optionalUser = usersRepository.findById(userId);

        if(!optionalUser.isPresent()) throw new UserIdNotFoundException();

        Users user = optionalUser.get();
        String oldEmail = user.getEmail();
        String newEmail = requestDto.getEmail();

        if(!oldEmail.equals(newEmail)) {
            if (usersRepository.findByEmail(newEmail).isPresent()) {
                throw new UserEmailInUseException();
            }
        }

        user.updateUserInfo(requestDto);
        user = usersRepository.save(user);

        return user;
    }

    @Transactional
    public JSONObject sendTemporaryPasswordEmail(String recipient) throws UserEmailNotFoundException, Exception {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        String temporaryPassword = PasswordGenerator.generate();
        if(usersRepository.findByEmail(recipient).isPresent()) {
            usersRepository.updatePassword(temporaryPassword, recipient);
            EmailContent emailContent = new EmailContent("[반창고] 임시 비밀번호 발급", "안녕하세요, 반창고 입니다!", "발급해드린 임시 비밀번호는 <span style='font-size: 20px'>" + temporaryPassword + "</span> 입니다.", "이 임시 비밀번호로 로그인 해주세요.", "로그인 하기", "dev.banchango.shop/login");
            Email.sendEmail(emailContent, recipient, senderEmail, senderEmailPassword, false);
            jsonObject.put("message", "임시 비밀번호 이메일이 정상적으로 전송되었습니다.");
            return jsonObject;
        } else throw new UserEmailNotFoundException();
    }
}