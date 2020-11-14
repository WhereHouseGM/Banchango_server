package com.banchango.warehouses.dto;

import com.banchango.domain.agencymainitemtypes.AgencyMainItemTypes;
import com.banchango.domain.agencywarehousedetails.AgencyWarehouseDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgencyWarehouseDetailInsertRequestDto {

    private String warehouseType;
    private String typeName;
    private AgencyWarehousePaymentInsertRequestDto[] payments;

    public AgencyWarehouseDetails toAgencyWarehouseDetailEntity(Integer warehouseId) {
        return AgencyWarehouseDetails.builder()
                .type(warehouseType).warehouseId(warehouseId)
                .build();
    }

    public AgencyMainItemTypes toAgencyMainItemsEntity(Integer agencyWarehouseDetailId) {
        return AgencyMainItemTypes.builder()
                .agencyWarehouseDetailId(agencyWarehouseDetailId)
                .name(typeName).build();
    }
}
