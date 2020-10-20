package com.banchango.warehouses.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewAgencyWarehousePaymentFormDto {
    private String unit;
    private int cost;
    private String description;
    private String[] deliveryCompanies;
}
