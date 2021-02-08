package com.banchango.estimateitems.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.exception.ForbiddenException;
import com.banchango.common.functions.warehouses.FindWarehouseById;
import com.banchango.domain.estimateitems.EstimateItem;
import com.banchango.domain.estimates.Estimate;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouse;
import com.banchango.estimateitems.dto.EstimateItemSearchDto;
import com.banchango.estimateitems.exception.EstimateItemNotFoundException;
import com.banchango.estimates.exception.EstimateNotFoundException;
import com.banchango.warehouses.exception.WarehouseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EstimateItemsService {
    private final EstimatesRepository estimatesRepository;
    private final FindWarehouseById findWarehouseById;

    @Transactional(readOnly = true)
    public List<EstimateItemSearchDto> getEstimateItemsByEstimateId(String accessToken, Integer estimateId) {
        Integer userId = JwtTokenUtil.extractUserId(accessToken);

        Estimate estimate = estimatesRepository.findById(estimateId).orElseThrow(EstimateNotFoundException::new);
        Warehouse warehouse = findWarehouseById.apply(estimate.getWarehouseId());

        if(warehouse.getStatus().equals(WarehouseStatus.DELETED)) throw new WarehouseNotFoundException("창고가 삭제됐습니다");

        if(!estimate.getUserId().equals(userId)) throw new ForbiddenException();

        List<EstimateItem> estimateItems = estimate.getEstimateItems();
        if(estimateItems.size() == 0) throw new EstimateItemNotFoundException();

        return estimate.getEstimateItems().stream()
                .map(EstimateItemSearchDto::new)
                .collect(Collectors.toList());
    }
}
