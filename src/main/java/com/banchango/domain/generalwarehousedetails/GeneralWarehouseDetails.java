package com.banchango.domain.generalwarehousedetails;

import lombok.Builder;
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

    @Builder
    public GeneralWarehouseDetails(Integer monthlyFee, Integer depositFee, Integer maintenanceFee, Integer minimumTerm, Integer warehouseId) {
        this.monthlyFee = monthlyFee;
        this.depositFee = depositFee;
        this.maintenanceFee = maintenanceFee;
        this.minimumTerm = minimumTerm;
        this.warehouseId = warehouseId;
    }
}
