package com.banchango.warehouses.dto;

import com.banchango.domain.mainitemtypes.ItemType;
import com.banchango.domain.warehouseconditions.WarehouseConditionType;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehouseimages.WarehouseImages;
import com.banchango.domain.warehouses.WarehouseType;
import com.banchango.domain.warehouses.Warehouses;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
public class WarehouseSearchDto {
    private String address;
    private Integer warehouseId;
    private List<WarehouseConditionType> warehouseCondition;
    private Integer minReleasePerMonth;
    private String name;
    private WarehouseType warehouseType;
    private String closeAt;
    private String mainImageUrl;
    private String openAt;
    private Integer space;
    private List<String> deliveryTypes;
    private List<WarehouseMainItemTypeMatchDto> mainItemTypes;

    public WarehouseSearchDto(Warehouses warehouse, String defaultImageUrl) {
        this(warehouse, defaultImageUrl, new ArrayList<>());
    }

    public WarehouseSearchDto(Warehouses warehouse, String defaultImageUrl, List<ItemType> queriedMainItemTypes) {
        List<WarehouseMainItemTypeMatchDto> mainItemTypes = warehouse.getMainItemTypes()
            .stream()
            .map(mainItemType -> new WarehouseMainItemTypeMatchDto(mainItemType.getType(), queriedMainItemTypes))
            .collect(Collectors.toList());

        // match가 true인 것들이 앞에 오도록 정렬시켜주는 함수
        sortMainItemTypeByMatchTrue(mainItemTypes);

        List<WarehouseConditionType> warehouseConditionNames = warehouse.getWarehouseConditions()
            .stream()
            .map(WarehouseConditions::getCondition)
            .collect(Collectors.toList());

        List<String> deliveryTypes = warehouse.getDeliveryTypes()
            .stream()
            .map(deliveryType -> deliveryType.getName())
            .collect(Collectors.toList());

        WarehouseImages mainImage = warehouse.getMainImage();

        this.address = warehouse.getAddress();
        this.warehouseId = warehouse.getId();
        this.warehouseCondition = warehouseConditionNames;
        this.minReleasePerMonth = warehouse.getMinReleasePerMonth();
        this.name = warehouse.getName();
        this.warehouseType = warehouse.getWarehouseType();
        this.closeAt = warehouse.getCloseAt();
        this.mainImageUrl = mainImage != null ? mainImage.getUrl() : defaultImageUrl;
        this.openAt = warehouse.getOpenAt();
        this.space = warehouse.getSpace();
        this.deliveryTypes = deliveryTypes;
        this.mainItemTypes = mainItemTypes;
    }

    private void sortMainItemTypeByMatchTrue(List<WarehouseMainItemTypeMatchDto> mainItemTypes) {
        mainItemTypes.sort((o1, o2) -> Boolean.compare(o2.getMatch(), o1.getMatch()));
    }
}
