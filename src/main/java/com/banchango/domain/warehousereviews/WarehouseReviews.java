package com.banchango.domain.warehousereviews;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_reviews")
public class WarehouseReviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
}
