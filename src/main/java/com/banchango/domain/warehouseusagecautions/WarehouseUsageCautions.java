package com.banchango.domain.warehouseusagecautions;

import com.banchango.domain.warehouses.Warehouses;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Setter
    private String content;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouses warehouse;

    @Builder
    public WarehouseUsageCautions(String content, Warehouses warehouse) {
        this.content = content;
        this.warehouse = warehouse;
    }
}
