package com.banchango.domain.deliverytypes;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;

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

    @Column(name = "agencywarehousedetailid")
    private Integer agencyWarehouseDetailId;

    public HashMap<String, Object> convertMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }
}
