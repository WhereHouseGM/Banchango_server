package com.banchango.domain.warehousefacilityusages;

import com.banchango.domain.warehouses.Warehouse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_facility_usages")
public class WarehouseFacilityUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    @Setter
    private String content;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Builder
    public WarehouseFacilityUsage(String content, Warehouse warehouse) {
        this.content = content;
        this.warehouse = warehouse;
    }
}
