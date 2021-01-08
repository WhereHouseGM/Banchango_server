package com.banchango.admin.dto;

import com.banchango.common.validator.ValueOfEnum;
import com.banchango.domain.estimates.EstimateStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EstimateStatusUpdateRequestDto {
    @ValueOfEnum(enumClass = EstimateStatus.class)
    @NotNull(message = "status 값이 주어지지 않았습니다")
    private EstimateStatus status;
}
