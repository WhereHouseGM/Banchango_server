package com.banchango.domain.warehouseusagecautions;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_usage_cautions")
public class WarehouseUsageCautions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false)
    private Integer warehouseId;

    @Builder
    public WarehouseUsageCautions(String content, Integer warehouseId) {
        this.content = content;
        this.warehouseId = warehouseId;
    }
}
