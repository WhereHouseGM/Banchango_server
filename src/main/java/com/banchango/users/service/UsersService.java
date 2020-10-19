package com.banchango.users.service;

import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.users.dto.UserSignupRequestDto;
import com.banchango.users.exception.UserEmailInUseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    @SuppressWarnings("unchecked")
    public Integer signUp(UserSignupRequestDto requestDto) throws Exception {
        if(usersRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new UserEmailInUseException();
        }
        Users user = new Users(requestDto);
        return usersRepository.save(user).getId();
    }
}
