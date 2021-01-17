package com.banchango.domain.securitycompanies;

import com.banchango.domain.warehouses.Warehouses;
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

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouses warehouse;

    @Builder
    public SecurityCompanies(String name, Warehouses warehouse) {
        this.name = name;
        this.warehouse = warehouse;
    }
}
