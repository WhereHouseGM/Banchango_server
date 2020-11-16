package com.banchango.domain.warehouseattachments;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "warehouse_attachments")
public class WarehouseAttachments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String url;

    @Column
    private Integer warehouseId;

    @Builder
    public WarehouseAttachments(String url, Integer warehouseId) {
        this.url = url;
        this.warehouseId = warehouseId;
    }
}
