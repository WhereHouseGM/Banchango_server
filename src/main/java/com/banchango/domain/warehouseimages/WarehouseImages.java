package com.banchango.domain.warehouseimages;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "warehouse_images")
public class WarehouseImages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Integer isMain;
}
