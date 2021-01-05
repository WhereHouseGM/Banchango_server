package com.banchango.warehouses.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WarehouseSummaryDto {
    private Integer id;
    private String name;
    private String address;

    @Builder
    public WarehouseSummaryDto(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
