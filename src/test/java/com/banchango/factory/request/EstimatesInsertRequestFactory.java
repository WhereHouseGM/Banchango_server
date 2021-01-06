package com.banchango.factory.request;

import com.banchango.estimates.dto.EstimateInsertRequestDto;
import com.banchango.estimates.dto.EstimateItemInsertRequestDto;

import java.util.ArrayList;
import java.util.List;

public class EstimatesInsertRequestFactory {
    public static EstimateInsertRequestDto create(Integer warehouseId) {
        List<EstimateItemInsertRequestDto> estimateItemInsertRequestDtos = new ArrayList<>();
        estimateItemInsertRequestDtos.add(EstimateItemsInsertRequestFactory.create());
        estimateItemInsertRequestDtos.add(EstimateItemsInsertRequestFactory.create());

        return EstimateInsertRequestDto.builder()
            .warehouseId(warehouseId)
            .content("content")
            .monthlyAverageRelease(1299)
            .estimateItems(estimateItemInsertRequestDtos)
            .build();
    }
}
