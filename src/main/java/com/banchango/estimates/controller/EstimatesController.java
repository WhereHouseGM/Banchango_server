package com.banchango.estimates.controller;

import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.estimates.dto.WarehouseEstimateInsertRequestDto;
import com.banchango.estimates.service.EstimatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class EstimatesController {
    private final EstimatesService estimatesService;

    @PostMapping("/v3/estimates")
    @ResponseStatus(HttpStatus.CREATED)
    @ValidateRequired
    public BasicMessageResponseDto saveEstimate(
        @RequestBody WarehouseEstimateInsertRequestDto warehouseEstimateInsertRequestDto,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        estimatesService.saveEstimate(accessToken, warehouseEstimateInsertRequestDto);

        return new BasicMessageResponseDto("견적 문의가 성공적으로 생성됐습니다");
    }
}
