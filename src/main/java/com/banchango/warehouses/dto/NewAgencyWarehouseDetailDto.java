package com.banchango.warehouses.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class NewAgencyWarehouseDetailDto {

    private String type;
    private String[] mainItemTypes;
    private String storageType;
    private List<NewAgencyWarehousePaymentFormDto> payments;
}
