package com.banchango.warehousereviews.dto;

import com.banchango.domain.warehousereviews.WarehouseReviews;
import com.banchango.tools.ObjectMaker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

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

    public JSONObject toJSONObject() {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("warehouseId", id);
        jsonObject.put("rating", rating);
        jsonObject.put("content", content);
        return jsonObject;
    }
}
