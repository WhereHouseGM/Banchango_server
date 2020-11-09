package com.banchango.users.service;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.tools.ObjectMaker;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.UserEmailInUseException;
import com.banchango.users.exception.UserIdNotFoundException;
import com.banchango.users.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public org.json.simple.JSONObject signUp(UserSignupRequestDto requestDto) throws Exception {
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
    @SuppressWarnings("unchecked")
    public org.json.simple.JSONObject signIn(UserSigninRequestDto requestDto) throws Exception{
        if(requestDto.getEmail() == null || requestDto.getPassword() == null) {
            throw new Exception();
        }
        Optional<Users> user = usersRepository.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
        if(user.isPresent()) {
            org.json.simple.JSONObject jsonObject = ObjectMaker.getJSONObjectWithUserInfo(user.get());
            jsonObject.put("accessToken", JwtTokenUtil.generateAccessToken(user.get().getUserId()));
            jsonObject.put("refreshToken", JwtTokenUtil.generateRefreshToken(user.get().getUserId()));
            jsonObject.put("tokenType", "Bearer");
            return jsonObject;
        }
        else {
            throw new UserNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public org.json.simple.JSONObject viewUserInfo(Integer userId, String token) throws Exception{
        if(JwtTokenUtil.validateTokenWithUserId(JwtTokenUtil.getToken(token), userId)) {
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
    @SuppressWarnings("unchecked")
    public org.json.simple.JSONObject updateUserInfo(Integer userId, UserSignupRequestDto requestDto, String token) throws Exception {
        if(!JwtTokenUtil.validateTokenWithUserId(JwtTokenUtil.getToken(token), userId)) {
            throw new AuthenticateException();
        }
        Optional<Users> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            if (usersRepository.findByEmail(requestDto.getEmail()).isPresent()) {
                throw new UserEmailInUseException();
            }
            Users user = optionalUser.get();
            user.updateUserInfo(requestDto);
            return ObjectMaker.getJSONObjectWithUserInfo(user);
        } else {
            throw new UserIdNotFoundException();
        }
    }
}
