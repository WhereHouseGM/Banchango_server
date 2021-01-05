package com.banchango.factory.entity;

import com.banchango.domain.estimateitems.EstimateBarcode;
import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimateitems.EstimateKeepingType;
import com.banchango.domain.estimates.Estimates;
import org.springframework.stereotype.Component;

@Component
public class EstimateItemEntityFactory {
    private static final Integer MONTHLY_AVERAGE_RELEASE = 1000;
    private static final EstimateBarcode BARCODE = EstimateBarcode.ALL;
    private static final Integer KEEPING_NUMBER = 32;
    private static final EstimateKeepingType KEEPING_TYPE = EstimateKeepingType.COLD;
    private static final Double PERIMETER = 303.3;
    private static final Integer SKU = 31;
    private static final String NAME = "NAME";
    private static final String URL = "https://www.google.com";
    private static final Double WEIGHT = 33.3;

    public EstimateItems create(Estimates estimate) {
        return EstimateItems.builder()
            .monthlyAverageRelease(MONTHLY_AVERAGE_RELEASE)
            .barcode(BARCODE)
            .keepingNumber(KEEPING_NUMBER)
            .keepingType(KEEPING_TYPE)
            .perimeter(PERIMETER)
            .sku(SKU)
            .name(NAME)
            .url(URL)
            .weight(WEIGHT)
            .estimate(estimate)
            .build();
    }
}
