package com.banchango.warehouses.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MyWarehousesResponseDto {
    private List<MyWarehouseDto> warehouses = new ArrayList();
}
