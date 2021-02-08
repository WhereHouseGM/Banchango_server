package com.banchango.domain.warehouseimages;

import com.banchango.domain.warehouses.Warehouse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "warehouse_images")
public class WarehouseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Boolean isMain;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Builder
    public WarehouseImage(String url, Boolean isMain, Warehouse warehouse) {
        this.url = url;
        this.isMain = isMain;
        this.warehouse = warehouse;
    }

    public boolean isMain() {
        return isMain;
    }
}
