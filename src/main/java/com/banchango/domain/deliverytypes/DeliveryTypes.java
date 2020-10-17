package com.banchango.domain.deliverytypes;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "delivery_types")
public class DeliveryTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String name;

    @Column(name = "agencyWarehouseDetailId")
    private Integer agencyWarehouseDetailId;
}
