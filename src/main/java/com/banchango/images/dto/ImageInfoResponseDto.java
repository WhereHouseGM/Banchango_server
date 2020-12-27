package com.banchango.images.dto;

import com.banchango.domain.warehouseimages.WarehouseImages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ImageInfoResponseDto {

    private String url;
    private Integer isMain;

    public ImageInfoResponseDto(WarehouseImages image) {
        this.url = image.getUrl();
        this.isMain = image.getIsMain();
    }
}
