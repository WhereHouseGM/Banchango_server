package com.banchango.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@NoArgsConstructor
@Getter
@Setter
public class UserSignupRequestDto {

    private String name;
    private String email;
    private String password;
    private String type;
    private String telephoneNumber;
    private String companyName;
    private String phoneNumber;

    public HashMap<String, Object> convertMap(int id) {
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
