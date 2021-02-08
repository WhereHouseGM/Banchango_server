package com.banchango.factory.entity;

import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.User;
import com.banchango.domain.users.UserRepository;
import com.banchango.domain.withdraws.Withdraw;
import com.banchango.domain.withdraws.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEntityFactory {
    private static final String NAME = "NAME";
    private static final String PASSWORD = "97210bf40747d347dc5664526548bd23c71a869bbdb87045dabb1971ef3ce1df";
    private static final String PHONE_NUMBER = "010-0000-0100";
    private static final String TELEPHONE_NUMBER = "02-0000-0100";
    private static final String COMPANY_NAME = "COMPANY_NAME";
    private static final String CAUSE = "CAUSE";

    private final UserRepository userRepository;
    private final WithdrawRepository withdrawRepository;
    private int countUsers = 0;

    @Autowired
    public UserEntityFactory(UserRepository userRepository, WithdrawRepository withdrawRepository) {
        this.userRepository = userRepository;
        this.withdrawRepository = withdrawRepository;
    }

    public User createUserWithOwnerType() {
        return create(UserRole.USER, UserType.OWNER);
    }

    public User createUserWithShipperType() {
        return create(UserRole.USER, UserType.SHIPPER);
    }

    public User createDeletedUserWithOwnerType() {
        User user = create(UserRole.USER, UserType.OWNER);

        Withdraw withdraw = Withdraw.builder()
            .userId(user.getUserId())
            .cause(CAUSE)
            .build();
        withdrawRepository.save(withdraw);

        return user;
    }

    public User createDeletedUserWithShipperType() {
        User user = create(UserRole.USER, UserType.SHIPPER);

        Withdraw withdraw = Withdraw.builder()
                .userId(user.getUserId())
                .cause(CAUSE)
                .build();
        withdrawRepository.save(withdraw);

        return user;
    }

    public User createAdminWithOwnerType() {
        return create(UserRole.ADMIN, UserType.OWNER);
    }

    public User createAdminWithShipperType() {
        return create(UserRole.ADMIN, UserType.SHIPPER);
    }

    private User create(UserRole role, UserType type) {
        User user = User.builder()
            .companyName(COMPANY_NAME)
            .email(generateEmail())
            .name(NAME)
            .password(PASSWORD)
            .telephoneNumber(TELEPHONE_NUMBER)
            .phoneNumber(PHONE_NUMBER)
            .type(type)
            .role(role)
            .build();

        userRepository.save(user);
        return user;
    }

    private String generateEmail() {
        return "EMAIL"+(countUsers++);
    }
}
