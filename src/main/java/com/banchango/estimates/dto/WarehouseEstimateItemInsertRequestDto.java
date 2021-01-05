package com.banchango.estimates.dto;

import com.banchango.domain.estimateitems.EstimateBarcode;
import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimateitems.EstimateKeepingType;
import com.banchango.domain.estimates.Estimates;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WarehouseEstimateItemInsertRequestDto {
    private String name;
    private Integer keepingNumber;
    private Double perimeter;
    private EstimateKeepingType keepingType;
    private Double weight;
    private EstimateBarcode barcode;
    private Integer sku;
    private String url;
    private Integer monthlyAverageRelease;

    @Builder
    public WarehouseEstimateItemInsertRequestDto(String name, Integer keepingNumber, Double perimeter, EstimateKeepingType keepingType, Double weight, EstimateBarcode barcode, Integer sku, String url, Integer monthlyAverageRelease) {
        this.name = name;
        this.keepingNumber = keepingNumber;
        this.perimeter = perimeter;
        this.keepingType = keepingType;
        this.weight = weight;
        this.barcode = barcode;
        this.sku = sku;
        this.url = url;
        this.monthlyAverageRelease = monthlyAverageRelease;
    }


    public EstimateItems toEntity(Estimates estimate) {
        return EstimateItems.builder()
            .name(name)
            .keepingNumber(keepingNumber)
            .perimeter(perimeter)
            .keepingType(keepingType)
            .weight(weight)
            .barcode(barcode)
            .sku(sku)
            .url(url)
            .monthlyAverageRelease(monthlyAverageRelease)
            .estimate(estimate)
            .build();
    }
}
