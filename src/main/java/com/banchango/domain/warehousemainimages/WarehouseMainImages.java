package com.banchango.domain.warehousemainimages;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_main_images")
public class WarehouseMainImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String mainImageUrl;

    @Column
    private Integer warehouseId;

    @Builder
    public WarehouseMainImages(String mainImageUrl, Integer warehouseId) {
        this.mainImageUrl = mainImageUrl;
        this.warehouseId = warehouseId;
    }
}
