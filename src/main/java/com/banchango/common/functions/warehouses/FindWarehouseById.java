package com.banchango.common.functions.warehouses;

import com.banchango.domain.warehouses.Warehouse;
import com.banchango.domain.warehouses.WarehouseRepository;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class FindWarehouseById implements Function<Integer, Warehouse> {

    private final WarehouseRepository warehouseRepository;

    @Override
    public Warehouse apply(Integer warehouseId) {
        return  warehouseRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
    }
}
