package com.banchango.warehousereviews.dto;

import com.banchango.domain.warehousereviews.WarehouseReviews;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@NoArgsConstructor
@Getter
@Setter
public class WarehouseReviewResponseDto {

    private int id;
    private int rating;
    private String content;
    private int userId;

    public WarehouseReviewResponseDto(WarehouseReviews reviews) {
        this.id = reviews.getId();
        this.rating = reviews.getRating();
        this.content = reviews.getContent();
        this.userId = reviews.getUserId();
    }

    public HashMap<String, Object> convertMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("rating", rating);
        map.put("content", content);
        return map;
    }
}
