package com.banchango.warehouses.service;

import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.tools.ObjectMaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WarehousesService {

    private final WarehousesRepository warehousesRepository;
    private final DeliveryTypesRepository deliveryTypesRepository;

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public org.json.simple.JSONObject getDeliveryTypes(){
        List<DeliveryTypes> list = deliveryTypesRepository.findAll();
        org.json.simple.JSONObject jsonObject = ObjectMaker.getSimpleJSONObject();
        org.json.simple.JSONArray jsonArray = ObjectMaker.getSimpleJSONArray();
        for(DeliveryTypes deliveryType : list) {
            org.json.simple.JSONObject jTemp = ObjectMaker.getSimpleJSONObject();
            jTemp.putAll(deliveryType.convertMap());
            jsonArray.add(jTemp);
        }
        jsonObject.put("types", jsonArray);
        return jsonObject;
    }
}
