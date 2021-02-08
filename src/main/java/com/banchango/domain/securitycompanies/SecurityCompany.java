package com.banchango.domain.securitycompanies;

import com.banchango.domain.warehouses.Warehouse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "security_companies")
public class SecurityCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    @Setter
    private String name;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Builder
    public SecurityCompany(String name, Warehouse warehouse) {
        this.name = name;
        this.warehouse = warehouse;
    }
}
