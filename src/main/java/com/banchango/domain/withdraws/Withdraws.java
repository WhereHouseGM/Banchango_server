package com.banchango.domain.withdraws;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
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

    @Builder
    public Withdraws(Integer userId, String cause) {
        this.userId = userId;
        this.cause = cause;
    }
}
