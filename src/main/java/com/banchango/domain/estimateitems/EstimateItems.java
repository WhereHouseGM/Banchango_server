package com.banchango.domain.estimateitems;

import com.banchango.domain.estimates.Estimates;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class EstimateItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer keepingNumber;

    @Column(nullable = false)
    private Double perimeter;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstimateKeepingType keepingType;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstimateBarcode barcode;

    @Column(nullable = false)
    private Integer sku;

    @Column(length = 1000)
    private String url;

    @ManyToOne
    @JoinColumn(name = "estimate_id")
    private Estimates estimate;

    @Builder
    public EstimateItems(String name, Integer keepingNumber, Double perimeter, EstimateKeepingType keepingType, Double weight, EstimateBarcode barcode, Integer sku, String url) {
        this.name = name;
        this.keepingNumber = keepingNumber;
        this.perimeter = perimeter;
        this.keepingType = keepingType;
        this.weight = weight;
        this.barcode = barcode;
        this.sku = sku;
        this.url = url;
    }
}
