package com.banchango.warehouses.service;

import com.banchango.auth.exception.AuthenticateException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.agencymainitemtypes.AgencyMainItemTypesRepository;
import com.banchango.domain.agencywarehousedetails.AgencyWarehouseDetails;
import com.banchango.domain.agencywarehousedetails.AgencyWarehouseDetailsRepository;
import com.banchango.domain.agencywarehousepayments.AgencyWarehousePaymentsRepository;
import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.insurances.Insurances;
import com.banchango.domain.insurances.InsurancesRepository;
import com.banchango.domain.warehouseattachments.WarehouseAttachmentsRepository;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsagesRepository;
import com.banchango.domain.warehouselocations.WarehouseLocationsRepository;
import com.banchango.domain.warehousemainimages.WarehouseMainImages;
import com.banchango.domain.warehousemainimages.WarehouseMainImagesRepository;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.domain.warehousetypes.WarehouseTypes;
import com.banchango.domain.warehousetypes.WarehouseTypesRepository;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautionsRepository;
import com.banchango.tools.ObjectMaker;
import com.banchango.warehouses.dto.*;
import com.banchango.warehouses.exception.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    private final WarehouseMainImagesRepository warehouseMainImagesRepository;
    private final WarehouseUsageCautionsRepository warehouseUsageCautionsRepository;
    private final WarehouseFacilityUsagesRepository warehouseFacilityUsagesRepository;

    @Value("${banchango.no_image.url}")
    private String noImageUrl;

    @Transactional
    public JSONObject saveAgencyWarehouse(AgencyWarehouseInsertRequestDto wrapperDto, String token) throws Exception{
       if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
           throw new AuthenticateException();
       }
       int userId = Integer.parseInt(JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token)));
       if(wrapperDto.getInsurance() != null) {
           int insuranceId = getSavedInsuranceId(wrapperDto.getInsurance().toEntity());
           Warehouses warehouse = toWarehouseEntityWithInsurance(wrapperDto, insuranceId, userId);
           int warehouseId = warehousesRepository.save(warehouse).getWarehouseId();
           saveWarehouseType(wrapperDto.getWarehouseCondition(), warehouseId);
           saveWarehouseFacilityUsages(wrapperDto.getWarehouseFacilityUsages(), warehouseId);
           saveWarehouseUsageCautions(wrapperDto.getWarehouseUsageCautions(), warehouseId);
           saveWarehouseLocation(wrapperDto.getLocation(), warehouseId);
           saveAgencyWarehouseDetailInformations(wrapperDto.getAgencyDetails(), warehouseId);
       } else {
           Warehouses warehouse = toWarehouseEntityWithoutInsurance(wrapperDto, userId);
           int warehouseId = warehousesRepository.save(warehouse).getWarehouseId();
           saveWarehouseType(wrapperDto.getWarehouseCondition(), warehouseId);
           saveWarehouseLocation(wrapperDto.getLocation(), warehouseId);
           saveAgencyWarehouseDetailInformations(wrapperDto.getAgencyDetails(), warehouseId);
       }
       JSONObject jsonObject = ObjectMaker.getJSONObject();
       jsonObject.put("message", "창고가 정상적으로 등록 되었습니다.");
       return jsonObject;
    }

    private Warehouses toWarehouseEntityWithInsurance(WarehouseInsertRequestDto wrapperDto, Integer insuranceId, Integer userId) {
        return Warehouses.builder()
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
    }

    private Warehouses toWarehouseEntityWithoutInsurance(WarehouseInsertRequestDto wrapperDto, Integer userId) {
        return Warehouses.builder()
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
    }


    private void saveWarehouseType(String[] warehouseConditions, Integer warehouseId) {
        for(String warehouseCondition : warehouseConditions) {
            warehouseTypesRepository.save(WarehouseTypes.builder().name(warehouseCondition).warehouseId(warehouseId).build());
        }
    }

    private void saveWarehouseFacilityUsages(String[] contents, Integer warehouseId) {
        for(String content : contents) {
            warehouseFacilityUsagesRepository.save(WarehouseFacilityUsages.builder().content(content).warehouseId(warehouseId).build());
        }
    }

    private void saveWarehouseUsageCautions(String[] contents, Integer warehouseId) {
        for(String content : contents) {
            warehouseUsageCautionsRepository.save(WarehouseUsageCautions.builder().content(content).warehouseId(warehouseId).build());
        }
    }

    private void saveWarehouseLocation(WarehouseLocationDto locationDto, Integer warehouseId) {
        warehouseLocationsRepository.save(locationDto.toEntity(warehouseId));
    }

    private void saveAgencyWarehouseDetailInformations(AgencyWarehouseDetailInsertRequestDto requestDto, Integer warehouseId) {
        int agencyWarehouseDetailId = getSavedAgencyWarehouseDetailId(requestDto.toAgencyWarehouseDetailEntity(warehouseId));
        agencyMainItemTypesRepository.save(requestDto.toAgencyMainItemsEntity(agencyWarehouseDetailId));
        saveWarehousePayments(requestDto.getPayments(), agencyWarehouseDetailId);
        saveDeliveryTypes(requestDto.getDeliveryTypes(), agencyWarehouseDetailId);
    }

    private void saveWarehousePayments(AgencyWarehousePaymentInsertRequestDto[] payments, Integer agencyWarehouseDetailId) {
        for(AgencyWarehousePaymentInsertRequestDto dto : payments) {
            agencyWarehousePaymentsRepository.save(dto.toEntity(agencyWarehouseDetailId));
        }
    }

    private void saveDeliveryTypes(String[] names, Integer agencyWarehouseDetailId) {
        for(String name : names) {
            deliveryTypesRepository.save(DeliveryTypes.builder().name(name).agencyWarehouseDetailId(agencyWarehouseDetailId).build());
        }
    }

    private Integer getSavedAgencyWarehouseDetailId(AgencyWarehouseDetails detail) {
        return agencyWarehouseDetailsRepository.save(detail).getAgencyWarehouseDetailId();
    }

    private Integer getSavedInsuranceId(Insurances insurance) {
        return insurancesRepository.save(insurance).getInsuranceId();
    }

    @Transactional(readOnly = true)
    public JSONObject search(String address, Integer limit, Integer offset) throws WarehouseSearchException{
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        PageRequest request = PageRequest.of(limit, offset);
        List<WarehouseSearchResponseDto> warehouses = warehousesRepository.findByAddressContaining(address, request).stream().map(WarehouseSearchResponseDto::new).collect(Collectors.toList());
        if(warehouses.size() == 0) throw new WarehouseSearchException();
        for(WarehouseSearchResponseDto searchResponseDto : warehouses) {
            WarehouseLocationDto locationDto = new WarehouseLocationDto(warehouseLocationsRepository.findByWarehouseId(searchResponseDto.getWarehouseId()));
            List<WarehouseTypesDto> typesDtos = warehouseTypesRepository.findByWarehouseId(searchResponseDto.getWarehouseId()).stream().map(WarehouseTypesDto::new).collect(Collectors.toList());
            Optional<WarehouseMainImages> imageOptional = warehouseMainImagesRepository.findByWarehouseId(searchResponseDto.getWarehouseId());
            if(imageOptional.isPresent()) {
                jsonArray.put(searchResponseDto.toJSONObject(locationDto, imageOptional.get().getMainImageUrl(), typesDtos));
            } else {
                jsonArray.put(searchResponseDto.toJSONObject(locationDto, noImageUrl, typesDtos));
            }
        }
        jsonObject.put("warehouses", jsonArray);
        return jsonObject;
    }

    @Transactional(readOnly = true)
    public JSONObject getAgencyWarehouseList(String mainItemType) throws Exception{
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        List<Integer> warehouseIdList = agencyMainItemTypesRepository.getWarehouseIdsByWarehouseType(mainItemType);
        if(warehouseIdList.size() == 0) throw new WarehouseNotFoundException();
        for(Integer warehouseId : warehouseIdList) {
            AgencyWarehouseListResponseDto dto = warehousesRepository.findByWarehouseId(warehouseId).map(AgencyWarehouseListResponseDto::new).orElseThrow(WarehouseIdNotFoundException::new);
            AgencyWarehouseDetails detail = agencyWarehouseDetailsRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
            dto.setWarehouseType(detail.getType());
            dto.setMinReleasePerMonth(detail.getMinReleasePerMonth());
            dto.setDeliveryTypes(new DeliveryTypeResponseDto(deliveryTypesRepository.findByAgencyWarehouseDetailId(detail.getAgencyWarehouseDetailId())).getDeliveryType());
            dto.setWarehouseConditions(warehouseTypesRepository.findByWarehouseId(warehouseId).stream().map(WarehouseTypesDto::new).collect(Collectors.toList()));
            Optional<WarehouseMainImages> imagesOptional = warehouseMainImagesRepository.findByWarehouseId(warehouseId);
            if (imagesOptional.isPresent()) {
                dto.setMainImageUrl(imagesOptional.get().getMainImageUrl());
            } else {
                dto.setMainImageUrl(noImageUrl);
            }
            JSONObject listObject = dto.toJSONObject();
            jsonArray.put(listObject);
        }
        jsonObject.put("warehouses", jsonArray);
        return jsonObject;
    }

    @Transactional
    public JSONObject delete(Integer warehouseId, String token) throws Exception {
        if(!JwtTokenUtil.isTokenValidated(JwtTokenUtil.getToken(token))) {
            throw new AuthenticateException();
        }
        Warehouses warehouse = warehousesRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        if(!warehouse.getUserId().equals(Integer.parseInt(JwtTokenUtil.extractUserId(JwtTokenUtil.getToken(token))))) {
            throw new WarehouseInvalidAccessException();
        }
        if(warehouse.getInsuranceId() != null) {
            insurancesRepository.deleteByInsuranceId(warehouse.getInsuranceId());
        }
        warehousesRepository.delete_(warehouseId);
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("message", "창고가 정상적으로 삭제되었습니다.");
        return jsonObject;
    }

    @Transactional(readOnly = true)
    public JSONObject getSpecificWarehouseInfo(Integer warehouseId) throws Exception {
        return createJSONObjectOfSpecificWarehouseInfo(warehouseId);
    }

    private JSONArray createJSONArrayOfWarehouseConditions(Integer warehouseId) {
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        List<WarehouseTypesDto> typesDtos = warehouseTypesRepository.findByWarehouseId(warehouseId).stream().map(WarehouseTypesDto::new).collect(Collectors.toList());
        for(WarehouseTypesDto dto : typesDtos) {
            jsonArray.put(dto.getName());
        }
        return jsonArray;
    }

    private JSONArray createJSONArrayOfWarehouseFacilityUsages(Integer warehouseId) {
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        List<WarehouseFacilityUsagesResponseDto> responseDtos = warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId).stream().map(WarehouseFacilityUsagesResponseDto::new).collect(Collectors.toList());
        for(WarehouseFacilityUsagesResponseDto dto : responseDtos) {
            jsonArray.put(dto.getContent());
        }
        return jsonArray;
    }

    private JSONArray createJSONArrayOfWarehouseUsageCautions(Integer warehouseId) {
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        List<WarehouseUsageCautionsResponseDto> responseDtos = warehouseUsageCautionsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseUsageCautionsResponseDto::new).collect(Collectors.toList());
        for(WarehouseUsageCautionsResponseDto dto : responseDtos) {
            jsonArray.put(dto.getContent());
        }
        return jsonArray;
    }

    private JSONObject createJSONObjectOfSpecificWarehouseInfo(Integer warehouseId) throws WarehouseIdNotFoundException {
        WarehouseResponseDto warehouseResponseDto = new WarehouseResponseDto(warehousesRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new));
        JSONObject jsonObject = warehouseResponseDto.toJSONObject();
        WarehouseLocationDto locationDto = new WarehouseLocationDto(warehouseLocationsRepository.findByWarehouseId(warehouseId));
        jsonObject.put("location", locationDto.toJSONObject());
        jsonObject.put("warehouseCondition", createJSONArrayOfWarehouseConditions(warehouseId));
        jsonObject.put("warehouseUsageCautions", createJSONArrayOfWarehouseUsageCautions(warehouseId));
        jsonObject.put("warehouseFacilityUsages", createJSONArrayOfWarehouseFacilityUsages(warehouseId));
        Integer agencyWarehouseDetailId = getAgencyWarehouseDetailId(warehouseId);
        Optional<WarehouseMainImages> imageOptional = warehouseMainImagesRepository.findByWarehouseId(warehouseId);
        if(imageOptional.isPresent()) {
            jsonObject.put("mainImageUrl", imageOptional.get().getMainImageUrl());
        } else {
            jsonObject.put("mainImageUrl", noImageUrl);
        }
        jsonObject.put("images", createJSONArrayOfAttachments(warehouseId));
        if(warehouseResponseDto.getInsuranceId() != null) {
            jsonObject.put("insuranceName", insurancesRepository.findByInsuranceId(warehouseResponseDto.getInsuranceId()).getName());
        }
        jsonObject.put("agencyDetails", createJSONObjectOfAgencyDetails(warehouseId, agencyWarehouseDetailId));
        return jsonObject;
    }

    private JSONArray createJSONArrayOfAttachments(Integer warehouseId) {
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        List<WarehouseAttachmentDto> attachmentDtos = warehouseAttachmentsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseAttachmentDto::new).collect(Collectors.toList());
        for(WarehouseAttachmentDto dto : attachmentDtos) {
            jsonArray.put(dto.getUrl());
        }
        return jsonArray;
    }

    private Integer getAgencyWarehouseDetailId(Integer warehouseId) throws WarehouseIdNotFoundException{
        return agencyWarehouseDetailsRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new).getAgencyWarehouseDetailId();
    }

    private JSONArray createJSONArrayOfDeliveryTypes(Integer agencyWarehouseDetailId) {
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        List<DeliveryTypes> deliveryTypes = deliveryTypesRepository.findByAgencyWarehouseDetailId(agencyWarehouseDetailId);
        for(DeliveryTypes type : deliveryTypes) {
            jsonArray.put(type.getName());
        }
        return jsonArray;
    }

    private JSONArray createJSONArrayOfPayments(Integer agencyWarehouseDetailId) {
        JSONArray jsonArray = ObjectMaker.getJSONArray();
        List<AgencyWarehousePaymentResponseDto> paymentList = agencyWarehousePaymentsRepository.findByAgencyWarehouseDetailId(agencyWarehouseDetailId).stream().map(AgencyWarehousePaymentResponseDto::new).collect(Collectors.toList());
        for(AgencyWarehousePaymentResponseDto dto : paymentList) {
            jsonArray.put(dto.toJSONObject());
        }
        return jsonArray;
    }

    private JSONObject createJSONObjectOfAgencyDetails(Integer warehouseId, Integer agencyWarehouseDetailId) throws WarehouseIdNotFoundException{
        JSONObject jsonObject = ObjectMaker.getJSONObject();
        jsonObject.put("agencyWarehouseDetailId", agencyWarehouseDetailId);
        jsonObject.put("warehouseType", agencyWarehouseDetailsRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new).getType());
        jsonObject.put("mainItemType", agencyMainItemTypesRepository.findByAgencyWarehouseDetailId(agencyWarehouseDetailId).getName());
        jsonObject.put("deliveryTypes", createJSONArrayOfDeliveryTypes(agencyWarehouseDetailId));
        jsonObject.put("payments", createJSONArrayOfPayments(agencyWarehouseDetailId));
        return jsonObject;
    }
}