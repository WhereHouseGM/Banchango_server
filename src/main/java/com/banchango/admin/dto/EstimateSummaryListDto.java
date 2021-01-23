package com.banchango.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class EstimateSummaryListDto {
    private List<EstimateSummaryDto> estimates;
}
