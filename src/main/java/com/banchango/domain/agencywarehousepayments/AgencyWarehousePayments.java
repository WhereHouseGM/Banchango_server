package com.banchango.domain.agencywarehousepayments;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "agency_warehouse_payments")
public class AgencyWarehousePayments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40)
    private String unit;

    @Column
    private Integer cost;

    @Column(length = 40)
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private AgencyWarehousePaymentType type;

    @Column
    private Integer agencyWarehouseDetailId;

    @Builder
    public AgencyWarehousePayments(String unit, Integer cost, String description, String type, Integer agencyWarehouseDetailId) {
        this.unit = unit;
        this.cost = cost;
        this.description = description;
        this.type = AgencyWarehousePaymentType.valueOf(type);
        this.agencyWarehouseDetailId = agencyWarehouseDetailId;
    }
}
