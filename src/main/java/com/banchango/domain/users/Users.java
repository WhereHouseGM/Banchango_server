package com.banchango.domain.users;

import com.banchango.domain.BaseTimeEntity;
import com.banchango.users.dto.UserSignupRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, length = 400)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(length = 100, nullable = false)
    private String companyName;

    @Column(length = 40)
    private String telephoneNumber;

    @Column(length = 40)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public void updateUserInfo(UserSignupRequestDto requestDto) {
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
        this.type = requestDto.getType();
        this.companyName = requestDto.getCompanyName();
        this.phoneNumber = requestDto.getPhoneNumber();
    }

    @Builder
    public Users(String name, String email, String password, UserType type, String companyName, String telephoneNumber, String phoneNumber, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.companyName = companyName;
        this.telephoneNumber = telephoneNumber;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
