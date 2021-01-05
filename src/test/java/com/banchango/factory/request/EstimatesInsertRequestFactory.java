package com.banchango.factory.request;

import com.banchango.estimates.dto.WarehouseEstimateInsertRequestDto;
import com.banchango.estimates.dto.WarehouseEstimateItemInsertRequestDto;

import java.util.ArrayList;
import java.util.List;

public class EstimatesInsertRequestFactory {
    public static WarehouseEstimateInsertRequestDto create(Integer warehouseId) {
        List<WarehouseEstimateItemInsertRequestDto> estimateItemInsertRequestDtos = new ArrayList<>();
        estimateItemInsertRequestDtos.add(EstimateItemsInsertRequestFactory.create());
        estimateItemInsertRequestDtos.add(EstimateItemsInsertRequestFactory.create());

        return WarehouseEstimateInsertRequestDto.builder()
            .warehouseId(warehouseId)
            .content("content")
            .estimateItems(estimateItemInsertRequestDtos)
            .build();
    }
}
