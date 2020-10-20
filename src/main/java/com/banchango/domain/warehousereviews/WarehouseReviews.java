package com.banchango.domain.warehousereviews;

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

    @Column(name = "writerid")
    private Integer writerId;

    @Column(name = "warehouseid")
    private Integer warehouseId;
}
