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
    private Integer id;

    @Column
    String type;

    @Column
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    @Column
    private Integer warehouseId;
}
