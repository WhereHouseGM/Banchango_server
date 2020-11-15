package com.banchango.warehouses.dto;

import com.banchango.domain.agencywarehousepayments.AgencyWarehousePaymentType;
import com.banchango.domain.agencywarehousepayments.AgencyWarehousePayments;
import com.banchango.tools.ObjectMaker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

@NoArgsConstructor
@Getter
@Setter
public class AgencyWarehousePaymentResponseDto {

    private String unit;
    private Integer cost;
    private String description;
    private AgencyWarehousePaymentType paymentType;


    public AgencyWarehousePaymentResponseDto(AgencyWarehousePayments payment) {
        this.unit = payment.getUnit();
        this.cost = payment.getCost();
        this.description = payment.getDescription();
        this.paymentType = payment.getType();
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("unit", unit);
        jsonObject.put("cost", cost);
        jsonObject.put("description", description);
        jsonObject.put("paymentType", paymentType);
        return jsonObject;
    }
}
