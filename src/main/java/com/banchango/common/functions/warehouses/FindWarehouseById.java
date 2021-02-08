package com.banchango.common.functions.warehouses;

import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class FindWarehouseById implements Function<Integer, Warehouses> {

    private final WarehousesRepository warehousesRepository;

    @Override
    public Warehouses apply(Integer warehouseId) {
        return  warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
    }
}
