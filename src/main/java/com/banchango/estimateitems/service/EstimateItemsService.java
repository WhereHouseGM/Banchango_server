package com.banchango.estimateitems.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.exception.ForbiddenException;
import com.banchango.common.functions.estimates.FindEstimateById;
import com.banchango.common.functions.warehouses.FindWarehouseById;
import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.estimateitems.dto.EstimateItemSearchDto;
import com.banchango.estimateitems.exception.EstimateItemNotFoundException;
import com.banchango.warehouses.exception.WarehouseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EstimateItemsService {

    private final FindWarehouseById findWarehouseById;
    private final FindEstimateById findEstimateById;

    @Transactional(readOnly = true)
    public List<EstimateItemSearchDto> getEstimateItemsByEstimateId(String accessToken, Integer estimateId) {
        Integer userId = JwtTokenUtil.extractUserId(accessToken);

        Estimates estimate = findEstimateById.apply(estimateId);
        Warehouses warehouse = findWarehouseById.apply(estimate.getWarehouseId());

        if(warehouse.getStatus().equals(WarehouseStatus.DELETED)) throw new WarehouseNotFoundException("창고가 삭제됐습니다");

        if(!estimate.getUserId().equals(userId)) throw new ForbiddenException();

        List<EstimateItems> estimateItems = estimate.getEstimateItems();
        if(estimateItems.size() == 0) throw new EstimateItemNotFoundException();

        return estimate.getEstimateItems().stream()
                .map(EstimateItemSearchDto::new)
                .collect(Collectors.toList());
    }
}
