package com.banchango.domain.users;

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

    @Column(nullable = false, unique = true, length = 10)
    private String name;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "telephoneNumber", length = 20)
    private String telephoneNumber;

    @Column(name = "companyName", length = 20)
    private String companyName;

    @Column(name = "phoneNumber", length = 20)
    private String phoneNumber;

    @Column(columnDefinition = "enum('USER', 'ADMIN') DEFAULT 'USER'")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
