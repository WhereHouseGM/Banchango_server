package com.banchango.domain.warehousereviews;

import com.banchango.tools.ObjectMaker;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_reviews")
public class WarehouseReviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 200)
    private String content;

    @Column
    private Integer userId;

    @Column
    private Integer warehouseId;

    @Builder
    public WarehouseReviews(Integer rating, String content, Integer userId, Integer warehouseId) {
        this.rating = rating;
        this.content = content;
        this.userId = userId;
        this.warehouseId = warehouseId;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("reviewId", reviewId);
        jsonObject.put("rating", rating);
        jsonObject.put("content", content);
        return jsonObject;
    }
}
