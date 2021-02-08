package com.banchango.estimateitems.controller;

import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.domain.users.UserType;
import com.banchango.estimateitems.dto.EstimateItemSearchResponseDto;
import com.banchango.estimateitems.service.EstimateItemsService;
import com.banchango.estimates.service.EstimatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class EstimateItemsApiController {
    private final EstimatesService estimatesService;
    private final EstimateItemsService estimateItemsService;

    @GetMapping("/v3/estimates/{estimateId}/items")
    @ResponseStatus(HttpStatus.OK)
    @ValidateRequired(types = UserType.SHIPPER)
    public EstimateItemSearchResponseDto getEstimates(
        @PathVariable Integer estimateId,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        return new EstimateItemSearchResponseDto(estimateItemsService.getEstimateItemsByEstimateId(accessToken, estimateId));
    }
}
