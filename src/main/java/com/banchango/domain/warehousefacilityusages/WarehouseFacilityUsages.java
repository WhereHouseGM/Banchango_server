package com.banchango.domain.warehousefacilityusages;

import com.banchango.domain.warehouses.Warehouses;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_facility_usages")
public class WarehouseFacilityUsages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouses warehouse;

    @Builder
    public WarehouseFacilityUsages(String content, Warehouses warehouse) {
        this.content = content;
        this.warehouse = warehouse;
    }
}
