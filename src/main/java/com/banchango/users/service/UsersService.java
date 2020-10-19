package com.banchango.users.service;

import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.tools.ObjectMaker;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.UserEmailInUseException;
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
    public Integer signUp(UserSignupRequestDto requestDto) throws Exception {
        if(usersRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new UserEmailInUseException();
        }
        Users user = new Users(requestDto);
        return usersRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public org.json.simple.JSONObject signIn(UserSigninRequestDto requestDto) throws Exception{
        if(requestDto.getEmail() == null || requestDto.getPassword() == null) {
            throw new Exception();
        }
        Optional<Users> user = usersRepository.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
        if(user.isPresent()) {
            org.json.simple.JSONObject jsonObject = ObjectMaker.getSimpleJSONObject();
            org.json.simple.JSONArray jsonArray = ObjectMaker.getSimpleJSONArray();
            org.json.simple.JSONObject jTemp = ObjectMaker.getSimpleJSONObject();
            jTemp.putAll(user.get().convertMap());
            jsonArray.add(jTemp);
            jsonObject.put("User", jsonArray);
            return jsonObject;
        }
        else {
            throw new UserNotFoundException();
        }
    }
}
