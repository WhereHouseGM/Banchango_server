package com.banchango.domain.users;

import com.banchango.users.dto.UserSignupRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;

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

    public void updateUserInfo(UserSignupRequestDto requestDto) {
        this.name = requestDto.getName();
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.type = UserType.valueOf(requestDto.getType());
        this.companyName = requestDto.getCompanyName();
        this.telephoneNumber = requestDto.getTelephoneNumber();
        this.phoneNumber = requestDto.getPhoneNumber();
    }

    @Builder
    public Users(String name, String email, String password, UserType userType, String telephoneNumber, String companyName, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = userType;
        this.telephoneNumber = telephoneNumber;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.role = UserRole.USER;
    }

    public HashMap<String, Object> convertMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("email", email);
        map.put("type", type);
        map.put("telephoneNumber", telephoneNumber);
        map.put("phoneNumber", phoneNumber);
        map.put("companyName", companyName);
        return map;
    }
}
