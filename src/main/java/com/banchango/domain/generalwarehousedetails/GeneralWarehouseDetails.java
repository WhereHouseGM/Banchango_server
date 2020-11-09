package com.banchango.domain.generalwarehousedetails;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "general_warehouse_details")
public class GeneralWarehouseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer monthlyFee;

    @Column
    private Integer depositFee;

    @Column
    private Integer maintenanceFee;

    @Column
    private Integer minimumTerm;

    @Column
    private Integer warehouseId;
}
