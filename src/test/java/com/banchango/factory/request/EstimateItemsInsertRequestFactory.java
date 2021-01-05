package com.banchango.factory.request;

import com.banchango.domain.estimateitems.EstimateBarcode;
import com.banchango.domain.estimateitems.EstimateKeepingType;
import com.banchango.domain.estimates.Estimates;
import com.banchango.estimates.dto.WarehouseEstimateInsertRequestDto;
import com.banchango.estimates.dto.WarehouseEstimateItemInsertRequestDto;

public class EstimateItemsInsertRequestFactory {
    public static WarehouseEstimateItemInsertRequestDto create() {
        return WarehouseEstimateItemInsertRequestDto.builder()
            .name("name0")
            .weight(11.1)
            .barcode(EstimateBarcode.ALL)
            .keepingNumber(33)
            .keepingType(EstimateKeepingType.WARM)
            .perimeter(33.3)
            .sku(100)
            .url("https://google.com")
            .monthlyAverageRelease(12)
            .build();
    }
}
