package com.banchango.warehouses.dto;

import com.banchango.domain.agencywarehousedetails.AgencyWarehouseDetails;
import com.banchango.domain.warehouses.AgencyWarehouseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AgencyWarehouseDetailResponseDto {

    private AgencyWarehouseType type;
    private Integer minReleasePerMonth;

    public AgencyWarehouseDetailResponseDto(AgencyWarehouseDetails detail) {
        this.type = detail.getType();
        this.minReleasePerMonth = detail.getMinReleasePerMonth();
    }
}
