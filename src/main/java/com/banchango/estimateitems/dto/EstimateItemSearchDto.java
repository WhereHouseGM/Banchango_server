package com.banchango.estimateitems.dto;

import com.banchango.domain.estimateitems.EstimateBarcode;
import com.banchango.domain.estimateitems.EstimateItem;
import com.banchango.domain.estimateitems.EstimateKeepingType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EstimateItemSearchDto {
    private Integer id;
    private String name;
    private Integer keepingNumber;
    private Double weight;
    private EstimateBarcode barcode;
    private Integer sku;
    private String url;
    private Double perimeter;
    private EstimateKeepingType keepingType;

    public EstimateItemSearchDto(EstimateItem estimateItem) {
        id = estimateItem.getId();
        name = estimateItem.getName();
        keepingNumber = estimateItem.getKeepingNumber();
        weight = estimateItem.getWeight();
        barcode = estimateItem.getBarcode();
        sku = estimateItem.getSku();
        url = estimateItem.getUrl();
        perimeter = estimateItem.getPerimeter();
        keepingType = estimateItem.getKeepingType();
    }
}
