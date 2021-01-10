package com.banchango.domain.mainitemtypes;

import com.banchango.domain.warehouses.Warehouses;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "main_item_types")
public class MainItemTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MainItemType type;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouses warehouse;

    @Builder
    public MainItemTypes(MainItemType mainItemType, Warehouses warehouse) {
        this.type = mainItemType;
        this.warehouse = warehouse;
    }
}
