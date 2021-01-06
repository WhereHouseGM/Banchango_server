package com.banchango.estimates.controller;

import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.estimates.dto.EstimateInsertRequestDto;
import com.banchango.estimates.dto.EstimateSearchDto;
import com.banchango.estimates.dto.EstimateSearchResponseDto;
import com.banchango.estimates.service.EstimatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class EstimatesApiController {
    private final EstimatesService estimatesService;

    @PostMapping("/v3/estimates")
    @ResponseStatus(HttpStatus.OK)
    @ValidateRequired
    public BasicMessageResponseDto saveEstimate(
        @RequestBody EstimateInsertRequestDto estimateInsertRequestDto,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        estimatesService.saveEstimate(accessToken, estimateInsertRequestDto);

        return new BasicMessageResponseDto("견적 문의가 성공적으로 생성됐습니다");
    }

    @GetMapping("/v3/users/{userId}/estimates")
    @ResponseStatus(HttpStatus.OK)
    @ValidateRequired
    public EstimateSearchResponseDto getEstimates(
        @PathVariable Integer userId,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        return new EstimateSearchResponseDto(estimatesService.getEstimatesByUserId(accessToken, userId));
    }
}
