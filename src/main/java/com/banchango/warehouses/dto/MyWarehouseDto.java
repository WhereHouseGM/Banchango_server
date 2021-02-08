package com.banchango.warehouses.dto;

import com.banchango.domain.warehouseimages.WarehouseImage;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MyWarehouseDto {
    private Integer id;
    private String name;
    private String address;
    private String addressDetail;
    private WarehouseStatus status;
    private String mainImageUrl;

    public MyWarehouseDto(Warehouse warehouse, String defaultImageUrl) {
        WarehouseImage mainImage = warehouse.getMainImage();

        this.id = warehouse.getId();
        this.name = warehouse.getName();
        this.address = warehouse.getAddress();
        this.addressDetail = warehouse.getAddressDetail();
        this.status = warehouse.getStatus();
        this.mainImageUrl = mainImage != null ? mainImage.getUrl() : defaultImageUrl;
    }
}
