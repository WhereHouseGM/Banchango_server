package com.banchango.common.functions;

import com.banchango.admin.exception.AdminInvalidAccessException;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class DoubleCheckAdminAccess implements Function<Integer, Void> {

    private final UsersRepository usersRepository;

    @Override
    public Void apply(Integer userId) {
        Users user = usersRepository.findById(userId).orElseThrow(AdminInvalidAccessException::new);
        if(!user.getRole().equals(UserRole.ADMIN)) throw new AdminInvalidAccessException();
        return null;
    }
}
