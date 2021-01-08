package com.banchango.estimateitems.controller;

import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.estimateitems.dto.EstimateItemSearchResponseDto;
import com.banchango.estimateitems.service.EsimateItemsService;
import com.banchango.estimates.service.EstimatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class EstimateItemsApiController {
    private final EstimatesService estimatesService;
    private final EsimateItemsService estimateItemsService;

    @GetMapping("/v3/estimates/{estimateId}/items")
    @ResponseStatus(HttpStatus.OK)
    @ValidateRequired
    public EstimateItemSearchResponseDto getEstimates(
        @PathVariable Integer estimateId,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        return new EstimateItemSearchResponseDto(estimateItemsService.getEstimateItemsByEstimateId(accessToken, estimateId));
    }
}
