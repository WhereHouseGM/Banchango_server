package com.banchango.factory.entity;

import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.withdraws.Withdraws;
import com.banchango.domain.withdraws.WithdrawsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEntityFactory {
    private static final String NAME = "NAME";
    private static final String PASSWORD = "97210bf40747d347dc5664526548bd23c71a869bbdb87045dabb1971ef3ce1df";
    private static final String PHONE_NUMBER = "010-0000-0100";
    private static final String TELEPHONE_NUMBER = "02-0000-0100";
    private static final UserType TYPE = UserType.OWNER;
    private static final String COMPANY_NAME = "COMPANY_NAME";
    private static final String CAUSE = "CAUSE";

    private final UsersRepository usersRepository;
    private final WithdrawsRepository withdrawsRepository;
    private int countUsers = 0;

    @Autowired
    public UserEntityFactory(UsersRepository usersRepository, WithdrawsRepository withdrawsRepository) {
        this.usersRepository = usersRepository;
        this.withdrawsRepository = withdrawsRepository;
    }

    public Users createUser() {
        return create(UserRole.USER);
    }

    public Users createDeletedUser() {
        Users user = create(UserRole.USER);

        Withdraws withdraw = Withdraws.builder()
            .userId(user.getUserId())
            .cause(CAUSE)
            .build();
        withdrawsRepository.save(withdraw);

        return user;
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
            .telephoneNumber(TELEPHONE_NUMBER)
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
