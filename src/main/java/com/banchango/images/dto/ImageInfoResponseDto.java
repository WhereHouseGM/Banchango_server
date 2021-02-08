package com.banchango.images.dto;

import com.banchango.domain.warehouseimages.WarehouseImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ImageInfoResponseDto {

    private String url;
    private Boolean isMain;

    public ImageInfoResponseDto(WarehouseImage image) {
        this.url = image.getUrl();
        this.isMain = image.getIsMain();
    }
}
