package com.banchango.admin.dto;

import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.EstimateStatusAndCreatedAtAndWarehouseIdProjection;
import com.banchango.domain.warehouses.WarehouseIdAndNameProjection;
import com.banchango.tools.DateConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EstimateSummaryDto {

    private Integer id;
    private EstimateStatus status;
    private Integer warehouseId;
    private String name;
    private String createdAt;

    public EstimateSummaryDto(EstimateStatusAndCreatedAtAndWarehouseIdProjection projection) {
        this.id = projection.getId();
        this.status = projection.getStatus();
        this.createdAt = DateConverter.convertDateWithTime(projection.getCreatedAt());
    }

    public void updateWarehouseInfo(WarehouseIdAndNameProjection projection) {
        this.warehouseId = projection.getId();
        this.name = projection.getName();
    }
}
