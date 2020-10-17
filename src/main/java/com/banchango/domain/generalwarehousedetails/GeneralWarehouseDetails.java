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

    @Column(name = "monthlyFee")
    private Integer monthlyFee;

    @Column(name = "depositFee")
    private Integer depositFee;

    @Column(name = "maintenanceFee")
    private Integer maintenanceFee;

    @Column(name = "minUseTerm")
    private Integer minUseTerm;

    @Column(name = "warehouseId")
    private Integer warehouseId;
}
