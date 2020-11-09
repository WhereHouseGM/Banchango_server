package com.banchango.warehouses.service;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.tools.ObjectMaker;
import com.banchango.warehouses.dto.NewAgencyWarehouseDetailDto;
import com.banchango.warehouses.dto.NewGeneralWarehouseDetailFormDto;
import com.banchango.warehouses.dto.NewWarehouseDetailDto;
import com.banchango.warehouses.dto.NewWarehouseFormDto;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WarehousesService {

    private final WarehousesRepository warehousesRepository;
    private final DeliveryTypesRepository deliveryTypesRepository;


    @Transactional
    @SuppressWarnings("unchecked")
    // TODO : Service code.
    public org.json.simple.JSONObject save(NewWarehouseFormDto newWarehouseFormDto) {
        NewWarehouseDetailDto dto = newWarehouseFormDto.getAdditionalInfo();
        org.json.simple.JSONObject jsonObject = ObjectMaker.getSimpleJSONObject();
        try {
            if (dto instanceof NewAgencyWarehouseDetailDto) {
                NewAgencyWarehouseDetailDto detailDto = (NewAgencyWarehouseDetailDto) dto;


            } else if (dto instanceof NewGeneralWarehouseDetailFormDto) {
                NewGeneralWarehouseDetailFormDto formDto = (NewGeneralWarehouseDetailFormDto) dto;
            } else {
                throw new IllegalArgumentException();
            }
        } catch(IllegalArgumentException exception) {
            jsonObject = ObjectMaker.getJSONObjectWithException(exception);
        }
        return jsonObject;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public org.json.simple.JSONObject getDeliveryTypes(String token) throws AuthenticateException{
        if(!JwtTokenUtil.validateToken(JwtTokenUtil.getToken(token))) {
            throw new AuthenticateException();
        }
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

    // TODO : 연관된 테이블들이 ON DELETE SET NULL 인데, 그래도 테스트 해보고 싶지만 더미데이터가 없어서 못함 ㅠ
    @Transactional
    public void delete(Integer warehouseId, String token) throws Exception {
        if(!JwtTokenUtil.validateToken(token)) {
            throw new AuthenticateException();
        }
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        warehousesRepository.delete(warehouse);
    }
}
