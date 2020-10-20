package com.banchango.warehousereviews.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WarehouseReviewInsertRequestDto {

    private int rating;
    private String content;
}
