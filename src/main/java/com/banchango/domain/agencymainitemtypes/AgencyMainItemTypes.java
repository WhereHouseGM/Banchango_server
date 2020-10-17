package com.banchango.domain.agencymainitemtypes;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "agency_main_item_types")
public class AgencyMainItemTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private ItemTypeName name;

    @Column(name = "agencyWarehouseDetailId")
    private Integer agencyWarehouseDetailId;
}
