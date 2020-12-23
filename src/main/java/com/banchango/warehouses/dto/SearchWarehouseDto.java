package com.banchango.warehouses.dto;

import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouseimages.WarehouseImages;
import com.banchango.domain.warehouses.Warehouses;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class SearchWarehouseDto {
    private Integer warehouseId;
    private String name;
    private Integer space;
    private String mainImageUrl;
    private Double latitude;
    private Double longitude;
    private List<WarehouseCondition> warehouseCondition;

    public SearchWarehouseDto(Warehouses warehouse, String defaultImageUrl) {
        List<WarehouseCondition> warehouseConditionNames = warehouse.getWarehouseConditions()
                .stream()
                .map(condition -> condition.getCondition())
                .collect(Collectors.toList());
        WarehouseImages mainImage = warehouse.getMainImage();

        String mainImageUrl = defaultImageUrl;
        if(mainImage != null) mainImageUrl = mainImage.getUrl();

        this.warehouseId = warehouse.getId();
        this.name = warehouse.getName();
        this.space = warehouse.getSpace();
        this.mainImageUrl = mainImageUrl;
        this.latitude = warehouse.getLatitude();
        this.longitude = warehouse.getLongitude();
        this.warehouseCondition = warehouseConditionNames;
    }
}