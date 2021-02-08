package com.banchango.common.functions.admin;

import com.banchango.admin.exception.AdminInvalidAccessException;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.User;
import com.banchango.domain.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class DoubleCheckAdminAccess implements Function<Integer, Void> {

    private final UserRepository userRepository;

    @Override
    public Void apply(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(AdminInvalidAccessException::new);
        if(!user.getRole().equals(UserRole.ADMIN)) throw new AdminInvalidAccessException();
        return null;
    }
}
