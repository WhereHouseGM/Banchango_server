package com.banchango.domain.warehouselocations;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_locations")
public class WarehouseLocations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private Integer warehouseId;

    @Builder
    public WarehouseLocations(Double latitude, Double longitude, Integer warehouseId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.warehouseId = warehouseId;
    }
}
