package com.banchango.domain.estimates;

import com.banchango.domain.BaseTimeEntity;
import com.banchango.domain.estimateitems.EstimateItems;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class Estimates extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimate_id")
    private Integer id;

    @Column(length = 1000)
    private String content;

    @Column(nullable = false)
    private Integer userId;

    @Column
    private Integer warehouseId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstimateStatus status;

    @Column(nullable = false)
    private Integer monthlyAverageRelease;

    @Setter
    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL)
    private List<EstimateItems> estimateItems = new ArrayList<>();

    @Builder
    public Estimates(String content, Integer userId, Integer warehouseId, EstimateStatus status, Integer monthlyAverageRelease, List<EstimateItems> estimateItems) {
        this.content = content;
        this.userId = userId;
        this.warehouseId = warehouseId;
        this.status = status;
        this.monthlyAverageRelease = monthlyAverageRelease;
        this.estimateItems = estimateItems;
    }

    public void updateStatus(EstimateStatus status) {
        this.status = status;
    }
}
