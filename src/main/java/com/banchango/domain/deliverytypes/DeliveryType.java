package com.banchango.domain.deliverytypes;

import com.banchango.domain.warehouses.Warehouse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "delivery_types")
public class DeliveryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    @Setter
    private String name;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Builder
    public DeliveryType(String name, Warehouse warehouse) {
        this.name = name;
        this.warehouse = warehouse;
    }
}
