package com.banchango.admin.dto;

import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimate;
import com.banchango.domain.users.Users;
import com.banchango.estimateitems.dto.EstimateItemSearchDto;
import com.banchango.users.dto.UserInfoResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class EstimateDetailResponseDto {
    private Integer id;
    private String content;
    private EstimateStatus status;
    private Integer monthlyAverageRelease;
    private UserInfoResponseDto user;
    private List<EstimateItemSearchDto> items;
    private String warehouseName;

    @Builder
    public EstimateDetailResponseDto(Estimate estimate, Users user, String warehouseName, boolean isDeleted) {
        this.id = estimate.getId();
        this.content = estimate.getContent();
        this.status = estimate.getStatus();
        this.monthlyAverageRelease = estimate.getMonthlyAverageRelease();
        this.user = new UserInfoResponseDto(user, isDeleted);
        this.warehouseName = warehouseName;
        this.items = estimate.getEstimateItems()
            .stream()
            .map(EstimateItemSearchDto::new)
            .collect(Collectors.toList());
    }
}
