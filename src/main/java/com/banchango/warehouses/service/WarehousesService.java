package com.banchango.warehouses.service;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.agencymainitemtypes.AgencyMainItemTypesRepository;
import com.banchango.domain.agencywarehousedetails.AgencyWarehouseDetails;
import com.banchango.domain.agencywarehousedetails.AgencyWarehouseDetailsRepository;
import com.banchango.domain.agencywarehousepayments.AgencyWarehousePaymentsRepository;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.insurances.Insurances;
import com.banchango.domain.insurances.InsurancesRepository;
import com.banchango.domain.warehouseattachments.WarehouseAttachmentsRepository;
import com.banchango.domain.warehouselocations.WarehouseLocationsRepository;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.domain.warehousetypes.WarehouseTypes;
import com.banchango.domain.warehousetypes.WarehouseTypesRepository;
import com.banchango.tools.ObjectMaker;
import com.banchango.warehouses.dto.*;
import com.banchango.warehouses.exception.WarehouseAlreadyRegisteredException;
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
    private final InsurancesRepository insurancesRepository;
    private final AgencyWarehouseDetailsRepository agencyWarehouseDetailsRepository;
    private final AgencyMainItemTypesRepository agencyMainItemTypesRepository;
    private final AgencyWarehousePaymentsRepository agencyWarehousePaymentsRepository;

    // DONE
    @Transactional
    public JSONObject save(AgencyWarehouseInsertRequestDto wrapperDto, String token) throws Exception{
       if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
           throw new AuthenticateException();
       }
       if(warehousesRepository.findByUserId(Integer.parseInt(JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token)))).isPresent()) {
           throw new WarehouseAlreadyRegisteredException();
       }
       int userId = Integer.parseInt(JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token)));
       if(wrapperDto.getInsurance() != null) {
           int insuranceId = getSavedInsuranceId(wrapperDto.getInsurance().toEntity());
           Warehouses warehouse = Warehouses.builder()
                   .canUse(wrapperDto.getCanUse()).name(wrapperDto.getName())
                   .insuranceId(insuranceId).serviceType(wrapperDto.getServiceType())
                   .landArea(wrapperDto.getLandArea()).totalArea(wrapperDto.getTotalArea())
                   .address(wrapperDto.getAddress()).addressDetail(wrapperDto.getAddressDetail())
                   .description(wrapperDto.getDescription()).availableWeekdays(wrapperDto.getAvailableWeekdays())
                   .openAt(wrapperDto.getOpenAt()).closeAt(wrapperDto.getCloseAt())
                   .availableTimeDetail(wrapperDto.getAvailableTimeDetail()).cctvExist(wrapperDto.getCctvExist())
                   .securityCompanyExist(wrapperDto.getSecurityCompanyExist()).securityCompanyName(wrapperDto.getSecurityCompanyName())
                   .doorLockExist(wrapperDto.getDoorLockExist()).airConditioningType(wrapperDto.getAirConditioningType())
                   .workerExist(wrapperDto.getWorkerExist()).canPickup(wrapperDto.getCanPickup())
                   .canPark(wrapperDto.getCanPark()).parkingScale(wrapperDto.getParkingScale())
                   .userId(userId).build();

           int warehouseId = warehousesRepository.save(warehouse).getWarehouseId();
           saveWarehouseType(wrapperDto.getWarehouseType(), warehouseId);
           saveWarehouseLocation(wrapperDto.getLocation(), warehouseId);
           saveAgencyWarehouseDetailInformations(wrapperDto.getAgencyDetail(), warehouseId);
       } else {
           Warehouses warehouse = Warehouses.builder()
                   .canUse(wrapperDto.getCanUse()).name(wrapperDto.getName())
                   .serviceType(wrapperDto.getServiceType())
                   .landArea(wrapperDto.getLandArea()).totalArea(wrapperDto.getTotalArea())
                   .address(wrapperDto.getAddress()).addressDetail(wrapperDto.getAddressDetail())
                   .description(wrapperDto.getDescription()).availableWeekdays(wrapperDto.getAvailableWeekdays())
                   .openAt(wrapperDto.getOpenAt()).closeAt(wrapperDto.getCloseAt())
                   .availableTimeDetail(wrapperDto.getAvailableTimeDetail()).cctvExist(wrapperDto.getCctvExist())
                   .securityCompanyExist(wrapperDto.getSecurityCompanyExist()).securityCompanyName(wrapperDto.getSecurityCompanyName())
                   .doorLockExist(wrapperDto.getDoorLockExist()).airConditioningType(wrapperDto.getAirConditioningType())
                   .workerExist(wrapperDto.getWorkerExist()).canPickup(wrapperDto.getCanPickup())
                   .canPark(wrapperDto.getCanPark()).parkingScale(wrapperDto.getParkingScale())
                   .userId(userId).build();
           int warehouseId = warehousesRepository.save(warehouse).getWarehouseId();
           saveWarehouseType(wrapperDto.getWarehouseType(), warehouseId);
           saveWarehouseLocation(wrapperDto.getLocation(), warehouseId);
           saveAgencyWarehouseDetailInformations(wrapperDto.getAgencyDetail(), warehouseId);
       }
       JSONObject jsonObject = ObjectMaker.getJSONObject();
       jsonObject.put("message", "창고가 정상적으로 등록 되었습니다.");
       return jsonObject;
    }

    private void saveWarehouseType(String warehouseType, Integer warehouseId) {
        warehouseTypesRepository.save(WarehouseTypes.builder().name(warehouseType).warehouseId(warehouseId).build());
    }
    private void saveWarehouseLocation(WarehouseLocationDto locationDto, Integer warehouseId) {
        warehouseLocationsRepository.save(locationDto.toEntity(warehouseId));
    }

    private void saveAgencyWarehouseDetailInformations(AgencyWarehouseDetailInsertRequestDto requestDto, Integer warehouseId) {
        int agencyWarehouseDetailId = getSavedAgencyWarehouseDetailId(requestDto.toAgencyWarehouseDetailEntity(warehouseId));
        agencyMainItemTypesRepository.save(requestDto.toAgencyMainItemsEntity(agencyWarehouseDetailId));
        saveWarehousePayments(requestDto.getPayments(), agencyWarehouseDetailId);
    }

    public void saveWarehousePayments(AgencyWarehousePaymentInsertRequestDto[] payments, Integer agencyWarehouseDetailId) {
        for(AgencyWarehousePaymentInsertRequestDto dto : payments) {
            agencyWarehousePaymentsRepository.save(dto.toEntity(agencyWarehouseDetailId));
        }
    }

    private Integer getSavedAgencyWarehouseDetailId(AgencyWarehouseDetails detail) {
        return agencyWarehouseDetailsRepository.save(detail).getAgencyWarehouseDetailId();
    }

    private Integer getSavedInsuranceId(Insurances insurance) {
        return insurancesRepository.save(insurance).getInsuranceId();
    }

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
        Warehouses warehouse = warehousesRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        if(warehouse.getInsuranceId() != null) {
            insurancesRepository.deleteByInsuranceId(warehouse.getInsuranceId());
        }
        String userIdOfToken = JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token));
        if(!warehouse.getUserId().equals(Integer.parseInt(userIdOfToken))) {
            throw new WarehouseInvalidAccessException();
        }
        warehousesRepository.deleteByWarehouseId(warehouseId);
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("message", "창고가 정상적으로 삭제되었습니다.");
        return jsonObject;
    }
}