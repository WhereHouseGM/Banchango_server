package com.banchango.warehouses.dto;

import com.banchango.domain.insurances.Insurances;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InsuranceInsertRequestDto {

    private Integer insuranceId;
    private String name;

    public Insurances toEntity() {
        return Insurances.builder()
                .name(name)
                .build();
    }
}
