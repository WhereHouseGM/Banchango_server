package com.banchango.domain.agencywarehousedetails;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "agency_warehouse_details")
public class AgencyWarehouseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer agencyWarehouseDetailId;

    @Column
    @Enumerated(EnumType.STRING)
    private AgencyWarehouseType type;

    @Column
    private Integer warehouseId;

    @Column
    private Integer minReleasePerMonth;

    @Builder
    public AgencyWarehouseDetails(String type, Integer warehouseId, Integer minReleasePerMonth) {
        this.type = AgencyWarehouseType.valueOf(type);
        this.warehouseId = warehouseId;
        this.minReleasePerMonth = minReleasePerMonth;
    }
}