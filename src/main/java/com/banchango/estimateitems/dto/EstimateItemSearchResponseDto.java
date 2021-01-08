package com.banchango.estimateitems.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EstimateItemSearchResponseDto {
    private List<EstimateItemSearchDto> estimateItems;
}
