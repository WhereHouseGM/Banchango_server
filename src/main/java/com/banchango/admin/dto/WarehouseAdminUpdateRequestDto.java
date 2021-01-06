package com.banchango.admin.dto;

import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.warehouses.dto.WarehouseInsertRequestDto;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class WarehouseAdminUpdateRequestDto extends WarehouseInsertRequestDto {

    @NotBlank(message = "Warehouse status is missing.")
    private WarehouseStatus status;
}
