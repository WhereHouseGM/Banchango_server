package com.banchango.admin.dto;

import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouse;
import com.banchango.tools.DateConverter;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WarehouseAdminDetailResponseDto extends WarehouseDetailResponseDto {
    private WarehouseStatus status;
    private String createdAt;

    public WarehouseAdminDetailResponseDto(Warehouse warehouse, String defaultImageUrl) {
        super(warehouse, defaultImageUrl);
        this.status = warehouse.getStatus();
        this.createdAt = DateConverter.convertDateWithTime(warehouse.getCreatedAt());
    }
}
