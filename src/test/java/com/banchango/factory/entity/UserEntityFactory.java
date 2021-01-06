package com.banchango.factory.entity;

import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEntityFactory {
    private static final String NAME = "NAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String PHONE_NUMBER = "010-0000-0100";
    private static final UserType TYPE = UserType.OWNER;
    private static final String COMPANY_NAME = "COMPANY_NAME";

    private UsersRepository usersRepository;
    private int countUsers = 0;

    @Autowired
    public UserEntityFactory(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users createUser() {
        return create(UserRole.USER);
    }

    public Users createAdmin() {
        return create(UserRole.ADMIN);
    }

    private Users create(UserRole role) {
        Users user = Users.builder()
            .companyName(COMPANY_NAME)
            .email(generateEmail())
            .name(NAME)
            .password(PASSWORD)
            .phoneNumber(PHONE_NUMBER)
            .type(TYPE)
            .role(role)
            .build();

        usersRepository.save(user);
        return user;
    }

    private String generateEmail() {
        return "EMAIL"+(countUsers++);
    }
}
