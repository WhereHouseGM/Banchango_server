package com.banchango.common.functions.users;

import com.banchango.domain.users.User;
import com.banchango.domain.users.UsersRepository;
import com.banchango.users.exception.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class FindUserById implements Function<Integer, User> {

    private final UsersRepository usersRepository;

    @Override
    public User apply(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(UserIdNotFoundException::new);
    }
}
