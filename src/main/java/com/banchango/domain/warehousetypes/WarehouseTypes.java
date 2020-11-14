package com.banchango.domain.warehousetypes;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_types")
public class WarehouseTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WarehouseTypeName name;

    @Column
    private Integer warehouseId;

    @Builder
    public WarehouseTypes(String name, Integer warehouseId) {
        this.name = WarehouseTypeName.valueOf(name);
        this.warehouseId = warehouseId;
    }
}
