package com.banchango.warehouses.dto;

import com.banchango.domain.agencywarehousepayments.AgencyWarehousePayments;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgencyWarehousePaymentInsertRequestDto {

    private String unit;
    private Integer cost;
    private String description;
    private String paymentType;

    public AgencyWarehousePayments toEntity(Integer agencyWarehouseDetailId) {
         return AgencyWarehousePayments.builder()
                 .unit(unit).cost(cost)
                 .description(description).type(paymentType)
                 .agencyWarehouseDetailId(agencyWarehouseDetailId)
                 .build();
    }
}
