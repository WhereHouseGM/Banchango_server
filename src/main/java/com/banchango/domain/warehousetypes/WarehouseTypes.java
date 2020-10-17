package com.banchango.domain.warehousetypes;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class WarehouseTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WarehouseTypeName name;

    @Column(name = "warehouseId")
    private Integer warehouseId;
}
