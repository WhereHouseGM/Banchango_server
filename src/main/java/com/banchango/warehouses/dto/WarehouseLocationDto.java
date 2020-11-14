package com.banchango.warehouses.dto;

import com.banchango.domain.warehouselocations.WarehouseLocations;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WarehouseLocationDto {

    private double latitude;
    private double longitude;

    public WarehouseLocationDto(WarehouseLocations location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public WarehouseLocations toEntity(Integer warehouseId) {
        return WarehouseLocations.builder()
                .latitude(latitude).longitude(longitude)
                .warehouseId(warehouseId).build();
    }
}