package com.banchango.domain.securitycompanies;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "security_companies")
public class SecurityCompanies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String name;

    @Builder
    public SecurityCompanies(String name) {
        this.name = name;
    }
}
