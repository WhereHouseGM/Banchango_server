package com.banchango.estimates.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.estimates.dto.EstimateInsertRequestDto;
import com.banchango.estimates.dto.EstimateSearchDto;
import com.banchango.estimates.dto.EstimateSearchResponseDto;
import com.banchango.users.exception.UserInvalidAccessException;
import com.banchango.warehouses.dto.WarehouseSummaryDto;
import com.banchango.warehouses.exception.WarehouseIsNotViewableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EstimatesService {
    private final EstimatesRepository estimatesRepository;
    private final WarehousesRepository warehousesRepository;

    @Transactional
    public void saveEstimate(String accessToken, EstimateInsertRequestDto estimateInsertRequestDto) {
        Integer userId = JwtTokenUtil.extractUserId(accessToken);

        WarehouseStatus status = warehousesRepository.findStatusById(estimateInsertRequestDto.getWarehouseId());
        if(!status.equals(WarehouseStatus.VIEWABLE)) throw new WarehouseIsNotViewableException();

        Estimates newEstimate = Estimates.builder()
            .content(estimateInsertRequestDto.getContent())
            .userId(userId)
            .warehouseId(estimateInsertRequestDto.getWarehouseId())
            .status(EstimateStatus.IN_PROGRESS)
            .build();

        estimatesRepository.save(newEstimate);

        List<EstimateItems> newEstimateItems = estimateInsertRequestDto.getEstimateItems()
            .stream()
            .map(estimateItemDto -> estimateItemDto.toEntity(newEstimate))
            .collect(Collectors.toList());

        newEstimate.setEstimateItems(newEstimateItems);
    }

    @Transactional(readOnly = true)
    public List<EstimateSearchDto> getEstimatesByUserId(String accessToken, Integer userId) {
        Integer userIdFromAccessToken = JwtTokenUtil.extractUserId(accessToken);

        if(!userIdFromAccessToken.equals(userId)) throw new UserInvalidAccessException();

        List<EstimateSearchDto> estimates = estimatesRepository.findAllByUserId(userId)
            .stream()
            .map(estimate -> {
                EstimateSearchDto estimateSearchResponseDto = new EstimateSearchDto(estimate);
                Optional<WarehouseSummaryDto> optionalWarehouseSummaryDto = warehousesRepository.findById(estimate.getWarehouseId(), WarehouseSummaryDto.class);
                if(optionalWarehouseSummaryDto.isPresent()) estimateSearchResponseDto.updateWarehouse(optionalWarehouseSummaryDto.get());
                return estimateSearchResponseDto;
            })
            .collect(Collectors.toList());

        return estimates;
    }
}
