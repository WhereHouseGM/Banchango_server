package com.banchango.factory.request;

import com.banchango.domain.estimateitems.EstimateBarcode;
import com.banchango.domain.estimateitems.EstimateKeepingType;
import com.banchango.estimates.dto.EstimateItemInsertRequestDto;

public class EstimateItemsInsertRequestFactory {
    public static EstimateItemInsertRequestDto create() {
        return EstimateItemInsertRequestDto.builder()
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
