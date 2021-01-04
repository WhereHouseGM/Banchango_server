package com.banchango.domain.warehouseusagecautions;

import com.banchango.domain.warehouses.Warehouses;
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

    @Builder
    public WarehouseUsageCautions(String content) {
        this.content = content;
    }
}
