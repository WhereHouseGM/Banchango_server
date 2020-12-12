package com.banchango.domain.agencymainitemtypes;

import lombok.Builder;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemTypeName name;

    @Column
    private Integer agencyWarehouseDetailId;

    @Builder
    public AgencyMainItemTypes(String name, Integer agencyWarehouseDetailId) {
        this.name = ItemTypeName.valueOf(name);
        this.agencyWarehouseDetailId = agencyWarehouseDetailId;
    }
}
