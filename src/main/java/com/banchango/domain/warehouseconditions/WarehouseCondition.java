package com.banchango.domain.warehouseconditions;

import com.banchango.domain.warehouses.Warehouse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_conditions")
public class WarehouseCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "name")
    @Enumerated(EnumType.STRING)
    @Setter
    private WarehouseConditionType condition;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Builder
    public WarehouseCondition(WarehouseConditionType condition, Warehouse warehouse) {
        this.condition=condition;
        this.warehouse = warehouse;
    }
}
