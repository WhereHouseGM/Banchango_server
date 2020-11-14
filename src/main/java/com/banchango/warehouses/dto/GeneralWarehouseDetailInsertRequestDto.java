package com.banchango.warehouses.dto;

import com.banchango.domain.generalwarehousedetails.GeneralWarehouseDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralWarehouseDetailInsertRequestDto {

    private Integer monthlyFee;
    private Integer depositFee;
    private Integer maintenanceFee;
    private Integer minimumTerm;

    public GeneralWarehouseDetails toEntity(Integer warehouseId) {
        return GeneralWarehouseDetails.builder()
                .monthlyFee(monthlyFee).depositFee(depositFee)
                .maintenanceFee(maintenanceFee).minimumTerm(minimumTerm)
                .warehouseId(warehouseId).build();
    }
}
