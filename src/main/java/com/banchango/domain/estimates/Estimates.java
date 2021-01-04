package com.banchango.domain.estimates;

import com.banchango.domain.estimateitems.EstimateItems;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Estimates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimate_id")
    private Integer id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer warehouseId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstimateStatus status;

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "estimate_id")
    private List<EstimateItems> estimateItems = new ArrayList<>();
}
