package com.banchango.domain.warehouseconditions;

import com.banchango.domain.warehouses.Warehouses;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_conditions")
public class WarehouseConditions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "name")
    @Enumerated(EnumType.STRING)
    private WarehouseCondition condition;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouses warehouse;

    @Builder
    public WarehouseConditions(WarehouseCondition condition, Warehouses warehouse) {
        this.condition = condition;
        this.warehouse = warehouse;
    }
}
