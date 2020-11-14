package com.banchango.domain.insurances;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Insurances {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer insuranceId;

    @Column(length = 100, nullable = false)
    private String name;

    @Builder
    public Insurances(String name) {
        this.name = name;
    }
}
