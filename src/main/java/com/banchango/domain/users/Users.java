package com.banchango.domain.users;

import com.banchango.users.dto.UserSignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "telephonenumber", length = 20)
    private String telephoneNumber;

    @Column(name = "companyname", length = 20)
    private String companyName;

    @Column(name = "phonenumber", length = 20)
    private String phoneNumber;

    @Column(columnDefinition = "enum('USER', 'ADMIN') DEFAULT 'USER'")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public Users(UserSignupRequestDto dto) {
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.type = UserType.valueOf(dto.getType());
        this.telephoneNumber = dto.getTelephoneNumber();
        this.companyName = dto.getCompanyName();
        this.phoneNumber = dto.getPhoneNumber();
        this.role = UserRole.USER;
    }
}
