package com.banchango.domain.agencywarehousedetails;

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
    String type;

    @Column
    private Integer warehouseId;
}