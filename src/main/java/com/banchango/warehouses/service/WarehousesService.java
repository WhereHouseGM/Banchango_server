package com.banchango.warehouses.service;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.warehouseattachments.WarehouseAttachmentsRepository;
import com.banchango.domain.warehouselocations.WarehouseLocationsRepository;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.domain.warehousetypes.WarehouseTypesRepository;
import com.banchango.tools.ObjectMaker;
import com.banchango.warehouses.dto.WarehouseAttachmentDto;
import com.banchango.warehouses.dto.WarehouseLocationDto;
import com.banchango.warehouses.dto.WarehouseSearchResponseDto;
import com.banchango.warehouses.dto.WarehouseTypesDto;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import com.banchango.warehouses.exception.WarehouseInvalidAccessException;
import com.banchango.warehouses.exception.WarehouseSearchException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WarehousesService {

    private final WarehousesRepository warehousesRepository;
    private final DeliveryTypesRepository deliveryTypesRepository;
    private final WarehouseLocationsRepository warehouseLocationsRepository;
    private final WarehouseTypesRepository warehouseTypesRepository;
    private final WarehouseAttachmentsRepository warehouseAttachmentsRepository;

    /*
    @Transactional
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

     */

    /*
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
     */

    @Transactional(readOnly = true)
    public JSONObject search(String address, Integer limit, Integer offset) throws WarehouseSearchException{
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        PageRequest request = PageRequest.of(limit, offset);
        List<WarehouseSearchResponseDto> warehouses = warehousesRepository.findByAddressContaining(address, request).stream().map(WarehouseSearchResponseDto::new).collect(Collectors.toList());
        if(warehouses.size() == 0) throw new WarehouseSearchException();
        for(WarehouseSearchResponseDto searchResponseDto : warehouses) {
            WarehouseLocationDto locationDto = new WarehouseLocationDto(warehouseLocationsRepository.findByWarehouseId(searchResponseDto.getWarehouseId()));
            WarehouseTypesDto typesDto = new WarehouseTypesDto(warehouseTypesRepository.findByWarehouseId(searchResponseDto.getWarehouseId()));
            List<WarehouseAttachmentDto> attachmentsList = warehouseAttachmentsRepository.findByWarehouseId(searchResponseDto.getWarehouseId()).stream().map(WarehouseAttachmentDto::new).collect(Collectors.toList());
            if(attachmentsList.size() != 0) {
                jsonArray.put(searchResponseDto.toJSONObjectWithLocationAndAttachmentAndType(locationDto, attachmentsList.get(0), typesDto));
            } else {
                jsonArray.put(searchResponseDto.toJSONObjectWithLocationAndType(locationDto, typesDto));
            }
        }
        jsonObject.put("warehouses", jsonArray);
        return jsonObject;
    }

    // TODO : 연관된 테이블들이 ON DELETE CASCADE, ON UPDATE CASCADE 인데, 그래도 테스트 해보고 싶지만 더미데이터가 없어서 못함 ㅠ
    @Transactional
    public JSONObject delete(Integer warehouseId, String token) throws Exception {
        if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
            throw new AuthenticateException();
        }
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        String userIdOfToken = JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token));
        if(!warehouseId.equals(Integer.parseInt(userIdOfToken))) {
            throw new WarehouseInvalidAccessException();
        }
        warehousesRepository.delete(warehouse);
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("message", "창고가 정상적으로 삭제되었습니다.");
        return jsonObject;
    }
}