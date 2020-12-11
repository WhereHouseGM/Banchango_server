package com.banchango.warehouses.dto;

import com.banchango.domain.deliverytypes.DeliveryTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class DeliveryTypeResponseDto {

    private String[] deliveryType;

    public DeliveryTypeResponseDto(List<DeliveryTypes> types) {
        this.deliveryType = new String[types.size()];
        for(int i = 0; i < types.size(); i++) {
            deliveryType[i] = types.get(i).getName();
        }
    }
}
