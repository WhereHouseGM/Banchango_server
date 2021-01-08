package com.banchango.domain.withdraws;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Withdraws {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false, length = 1000)
    private String cause;
}
