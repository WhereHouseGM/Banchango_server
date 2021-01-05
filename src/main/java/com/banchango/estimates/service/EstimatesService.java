package com.banchango.estimates.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.estimates.dto.WarehouseEstimateInsertRequestDto;
import com.banchango.warehouses.exception.WarehouseIsNotViewableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EstimatesService {
    private final EstimatesRepository estimatesRepository;
    private final WarehousesRepository warehousesRepository;

    @Transactional
    public void saveEstimate(String accessToken, WarehouseEstimateInsertRequestDto warehouseEstimateInsertRequestDto) {
        Integer userId = JwtTokenUtil.extractUserId(accessToken);

        WarehouseStatus status = warehousesRepository.findStatusById(warehouseEstimateInsertRequestDto.getWarehouseId());
        if(status != WarehouseStatus.VIEWABLE) throw new WarehouseIsNotViewableException();

        List<EstimateItems> newEstimateItems = warehouseEstimateInsertRequestDto.getEstimateItems()
            .stream()
            .map(estimateItemDto -> estimateItemDto.toEntity())
            .collect(Collectors.toList());

        Estimates newEstimate = Estimates.builder()
            .content(warehouseEstimateInsertRequestDto.getContent())
            .userId(userId)
            .warehouseId(warehouseEstimateInsertRequestDto.getWarehouseId())
            .status(EstimateStatus.IN_PROGRESS)
            .estimateItems(newEstimateItems)
            .build();

        estimatesRepository.save(newEstimate);
    }
}
