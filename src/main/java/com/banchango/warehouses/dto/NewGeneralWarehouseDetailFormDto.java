package com.banchango.warehouses.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewGeneralWarehouseDetailFormDto extends NewWarehouseDetailDto{
    private int montylyFee;
    private int depositFee;
    private int maintenanceFee;
    private int minUseTerm;
}
