package com.banchango.factory.entity;

import com.banchango.domain.estimateitems.EstimateBarcode;
import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimateitems.EstimateKeepingType;
import com.banchango.domain.estimates.Estimates;
import org.springframework.stereotype.Component;

@Component
public class EstimateItemEntityFactory {
    public static final Integer MONTHLY_AVERAGE_RELEASE = 1000;
    public static final EstimateBarcode BARCODE = EstimateBarcode.ALL;
    public static final Integer KEEPING_NUMBER = 32;
    public static final EstimateKeepingType KEEPING_TYPE = EstimateKeepingType.COLD;
    public static final Double PERIMETER = 303.3;
    public static final Integer SKU = 31;
    public static final String NAME = "NAME";
    public static final String URL = "https://www.google.com";
    public static final Double WEIGHT = 33.3;

    public EstimateItems create(Estimates estimate) {
        return EstimateItems.builder()
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
