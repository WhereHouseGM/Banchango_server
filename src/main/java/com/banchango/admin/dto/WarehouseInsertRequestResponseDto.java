package com.banchango.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WarehouseInsertRequestResponseDto {

    private Integer warehouseId;
    private String name;
    private String createdAt;

    // TODO : LocalDatetime to string
}
