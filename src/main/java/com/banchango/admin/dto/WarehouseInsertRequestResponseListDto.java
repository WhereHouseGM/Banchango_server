package com.banchango.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class WarehouseInsertRequestResponseListDto {
    private List<WarehouseInsertRequestResponseDto> requests;

    @Builder
    public WarehouseInsertRequestResponseListDto(List<WarehouseInsertRequestResponseDto> requests) {
        this.requests = requests;
    }
}
