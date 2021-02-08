package com.banchango.common.functions.users;

import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.users.exception.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class FindUserById implements Function<Integer, Users> {

    private final UsersRepository usersRepository;

    @Override
    public Users apply(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(UserIdNotFoundException::new);
    }
}
