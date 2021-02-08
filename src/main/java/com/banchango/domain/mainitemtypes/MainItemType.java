package com.banchango.domain.mainitemtypes;

import com.banchango.domain.warehouses.Warehouse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "main_item_types")
public class MainItemType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private ItemType type;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Builder
    public MainItemType(ItemType mainItemType, Warehouse warehouse) {
        this.type = mainItemType;
        this.warehouse = warehouse;
    }
}
