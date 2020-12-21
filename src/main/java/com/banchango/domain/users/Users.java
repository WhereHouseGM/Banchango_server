package com.banchango.domain.users;

import com.banchango.domain.BaseTimeEntity;
import com.banchango.tools.ObjectMaker;
import com.banchango.users.dto.UserSignupRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(length = 20)
    private String telephoneNumber;

    @Column(length = 20)
    private String companyName;

    @Column(length = 20)
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

    public JSONObject toJSONObject() {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("name", name);
        jsonObject.put("email", email);
        jsonObject.put("type", type);
        jsonObject.put("telephoneNumber", telephoneNumber);
        jsonObject.put("phoneNumber", phoneNumber);
        jsonObject.put("companyName", companyName);
        return jsonObject;
    }
}
