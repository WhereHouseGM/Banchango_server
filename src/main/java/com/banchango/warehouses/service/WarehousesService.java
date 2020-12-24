package com.banchango.warehouses.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehouseconditions.WarehouseConditionsRepository;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsagesRepository;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautionsRepository;
import com.banchango.warehouses.dto.NewWarehouseRequestDto;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import com.banchango.warehouses.exception.WarehouseInvalidAccessException;
import com.banchango.warehouses.dto.WarehouseSearchDto;
import com.banchango.warehouses.exception.WarehouseNotFoundException;
import com.banchango.warehouses.exception.WarehouseSearchException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final WarehouseConditionsRepository warehouseConditionsRepository;
    private final WarehouseUsageCautionsRepository warehouseUsageCautionsRepository;
    private final WarehouseFacilityUsagesRepository warehouseFacilityUsagesRepository;

    @Value("${banchango.no_image.url}")
    private String noImageUrl;

    @Transactional
    public void saveAgencyWarehouse(NewWarehouseRequestDto newWarehouseRequestDto, String accessToken) {
        UserRole role = JwtTokenUtil.extractUserRole(accessToken);
        int userId = JwtTokenUtil.extractUserId(accessToken);

        Warehouses warehouse = Warehouses.builder()
                .userId(userId)
                .name(newWarehouseRequestDto.getName())
                .space(newWarehouseRequestDto.getSpace())
                .address(newWarehouseRequestDto.getAddress())
                .addressDetail(newWarehouseRequestDto.getAddressDetail())
                .description(newWarehouseRequestDto.getDescription())
                .availableWeekdays(newWarehouseRequestDto.getAvailableWeekdays())
                .openAt(newWarehouseRequestDto.getOpenAt())
                .closeAt(newWarehouseRequestDto.getCloseAt())
                .availableTimeDetail(newWarehouseRequestDto.getAvailableTimeDetail())
                .insurance(newWarehouseRequestDto.getInsurance())
                .cctvExist(newWarehouseRequestDto.getCctvExist())
                .securityCompanyName(newWarehouseRequestDto.getSecurityCompanyName())
                .doorLockExist(newWarehouseRequestDto.getDoorLockExist())
                .airConditioningType(newWarehouseRequestDto.getAirConditioningType())
                .workerExist(newWarehouseRequestDto.getWorkerExist())
                .canPickup(newWarehouseRequestDto.getCanPickup())
                .canPark(newWarehouseRequestDto.getCanPark())
                .mainItemType(newWarehouseRequestDto.getMainItemType())
                .warehouseType(newWarehouseRequestDto.getWarehouseType())
                .minReleasePerMonth(newWarehouseRequestDto.getMinReleasePerMonth())
                .latitude(newWarehouseRequestDto.getLatitude())
                .longitude(newWarehouseRequestDto.getLongitude())
                .build();

        List<DeliveryTypes> deliveryTypes = newWarehouseRequestDto.getDeliveryTypes().stream()
                .map(name -> new DeliveryTypes(name)).collect(Collectors.toList());
        warehouse.setDeliveryTypes(deliveryTypes);

        List<WarehouseConditions> warehouseConditions = newWarehouseRequestDto.getWarehouseCondition().stream()
                .map(condition -> new WarehouseConditions(condition)).collect(Collectors.toList());
        warehouse.setWarehouseConditions(warehouseConditions);

        List<WarehouseFacilityUsages> warehouseFacilityUsages = newWarehouseRequestDto.getWarehouseFacilityUsages().stream()
                .map(content -> new WarehouseFacilityUsages(content)).collect(Collectors.toList());
        warehouse.setWarehouseFacilityUsages(warehouseFacilityUsages);

        List<WarehouseUsageCautions> warehouseUsageCautions = newWarehouseRequestDto.getWarehouseUsageCautions().stream()
                .map(content -> new WarehouseUsageCautions(content)).collect(Collectors.toList());
        warehouse.setWarehouseUsageCautions(warehouseUsageCautions);

        warehousesRepository.save(warehouse);
    }

//    private Warehouses toWarehouseEntityWithInsurance(WarehouseInsertRequestDto wrapperDto, Integer insuranceId, Integer userId) {
//        return Warehouses.builder()
//                .canUse(wrapperDto.getCanUse()).name(wrapperDto.getName())
//                .insuranceId(insuranceId).serviceType(wrapperDto.getServiceType())
//                .landArea(wrapperDto.getLandArea()).totalArea(wrapperDto.getTotalArea())
//                .address(wrapperDto.getAddress()).addressDetail(wrapperDto.getAddressDetail())
//                .description(wrapperDto.getDescription()).availableWeekdays(wrapperDto.getAvailableWeekdays())
//                .openAt(wrapperDto.getOpenAt()).closeAt(wrapperDto.getCloseAt())
//                .availableTimeDetail(wrapperDto.getAvailableTimeDetail()).cctvExist(wrapperDto.getCctvExist())
//                .securityCompanyExist(wrapperDto.getSecurityCompanyExist()).securityCompanyName(wrapperDto.getSecurityCompanyName())
//                .doorLockExist(wrapperDto.getDoorLockExist()).airConditioningType(wrapperDto.getAirConditioningType())
//                .workerExist(wrapperDto.getWorkerExist()).canPickup(wrapperDto.getCanPickup())
//                .canPark(wrapperDto.getCanPark()).parkingScale(wrapperDto.getParkingScale())
//                .userId(userId).build();
//    }
//
//    private Warehouses toWarehouseEntityWithoutInsurance(WarehouseInsertRequestDto wrapperDto, Integer userId) {
//        return Warehouses.builder()
//                .canUse(wrapperDto.getCanUse()).name(wrapperDto.getName())
//                .serviceType(wrapperDto.getServiceType())
//                .landArea(wrapperDto.getLandArea()).totalArea(wrapperDto.getTotalArea())
//                .address(wrapperDto.getAddress()).addressDetail(wrapperDto.getAddressDetail())
//                .description(wrapperDto.getDescription()).availableWeekdays(wrapperDto.getAvailableWeekdays())
//                .openAt(wrapperDto.getOpenAt()).closeAt(wrapperDto.getCloseAt())
//                .availableTimeDetail(wrapperDto.getAvailableTimeDetail()).cctvExist(wrapperDto.getCctvExist())
//                .securityCompanyExist(wrapperDto.getSecurityCompanyExist()).securityCompanyName(wrapperDto.getSecurityCompanyName())
//                .doorLockExist(wrapperDto.getDoorLockExist()).airConditioningType(wrapperDto.getAirConditioningType())
//                .workerExist(wrapperDto.getWorkerExist()).canPickup(wrapperDto.getCanPickup())
//                .canPark(wrapperDto.getCanPark()).parkingScale(wrapperDto.getParkingScale())
//                .userId(userId).build();
//    }
//
//
//    private void saveWarehouseType(String[] warehouseConditions, Integer warehouseId) {
//        for(String warehouseCondition : warehouseConditions) {
//            warehouseConditionsRepository.save(WarehouseConditions.builder().name(warehouseCondition).warehouseId(warehouseId).build());
//        }
//    }
//
//    private void saveWarehouseFacilityUsages(String[] contents, Integer warehouseId) {
//        for(String content : contents) {
//            warehouseFacilityUsagesRepository.save(WarehouseFacilityUsages.builder().content(content).warehouseId(warehouseId).build());
//        }
//    }
//
//    private void saveWarehouseUsageCautions(String[] contents, Integer warehouseId) {
//        for(String content : contents) {
//            warehouseUsageCautionsRepository.save(WarehouseUsageCautions.builder().content(content).warehouseId(warehouseId).build());
//        }
//    }
//
//    private void saveWarehouseLocation(WarehouseLocationDto locationDto, Integer warehouseId) {
//        warehouseLocationsRepository.save(locationDto.toEntity(warehouseId));
//    }
//
//    private void saveAgencyWarehouseDetailInformations(AgencyWarehouseDetailInsertRequestDto requestDto, Integer warehouseId) {
//        int agencyWarehouseDetailId = getSavedAgencyWarehouseDetailId(requestDto.toAgencyWarehouseDetailEntity(warehouseId));
//        agencyMainItemTypesRepository.save(requestDto.toAgencyMainItemsEntity(agencyWarehouseDetailId));
//        saveDeliveryTypes(requestDto.getDeliveryTypes(), agencyWarehouseDetailId);
//    }
//
//    private void saveDeliveryTypes(String[] names, Integer agencyWarehouseDetailId) {
//        for(String name : names) {
//            deliveryTypesRepository.save(DeliveryTypes.builder().name(name).agencyWarehouseDetailId(agencyWarehouseDetailId).build());
//        }
//    }
//
//    private Integer getSavedAgencyWarehouseDetailId(AgencyWarehouseDetails detail) {
//        return agencyWarehouseDetailsRepository.save(detail).getAgencyWarehouseDetailId();
//    }
//
//    private Integer getSavedInsuranceId(Insurances insurance) {
//        return insurancesRepository.save(insurance).getInsuranceId();
//    }
//
    @Transactional(readOnly = true)
    public List<WarehouseSearchDto> searchWarehouses(String address, PageRequest pageRequest) {
        List<WarehouseSearchDto> warehouses = warehousesRepository.findByAddressContaining(address, pageRequest)
                .stream()
                .map(warehouse -> new WarehouseSearchDto(warehouse, noImageUrl))
                .collect(Collectors.toList());

        if(warehouses.size() == 0) throw new WarehouseSearchException();

        return warehouses;
    }

//    @Transactional(readOnly = true)
//    public JSONObject getAgencyWarehouseList(String mainItemType) throws Exception{
//        JSONObject jsonObject = ObjectMaker.getJSONObject();
//        JSONArray jsonArray = ObjectMaker.getJSONArray();
//        List<Integer> warehouseIdList = agencyMainItemTypesRepository.getWarehouseIdsByWarehouseType(mainItemType);
//        if(warehouseIdList.size() == 0) throw new WarehouseNotFoundException();
//        for(Integer warehouseId : warehouseIdList) {
//            AgencyWarehouseListResponseDto dto = warehousesRepository.findByWarehouseId(warehouseId).map(AgencyWarehouseListResponseDto::new).orElseThrow(WarehouseIdNotFoundException::new);
//            AgencyWarehouseDetails detail = agencyWarehouseDetailsRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
//            dto.setWarehouseType(detail.getType());
//            dto.setMinReleasePerMonth(detail.getMinReleasePerMonth());
//            dto.setDeliveryTypes(new DeliveryTypeResponseDto(deliveryTypesRepository.findByAgencyWarehouseDetailId(detail.getAgencyWarehouseDetailId())).getDeliveryType());
//            dto.setWarehouseConditions(warehouseConditionsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseTypesDto::new).collect(Collectors.toList()));
//            Optional<WarehouseMainImages> imagesOptional = warehouseMainImagesRepository.findByWarehouseId(warehouseId);
//            if (imagesOptional.isPresent()) {
//                dto.setMainImageUrl(imagesOptional.get().getMainImageUrl());
//            } else {
//                dto.setMainImageUrl(noImageUrl);
//            }
//            JSONObject listObject = dto.toJSONObject(mainItemType);
//            jsonArray.put(listObject);
//        }
//        jsonObject.put("warehouses", jsonArray);
//        return jsonObject;
//    }
//
    @Transactional(readOnly = true)
    public List<WarehouseSearchDto> getWarehouses(PageRequest pageRequest) {
        List<WarehouseSearchDto> warehouses = warehousesRepository.findAll(pageRequest).getContent()
                .stream()
                .map(warehouse -> new WarehouseSearchDto(warehouse, noImageUrl))
                .collect(Collectors.toList());

        if(warehouses.size() == 0) throw new WarehouseNotFoundException();

        return warehouses;
    }

    @Transactional
    public void delete(Integer warehouseId, String accessToken) {
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);

        int accessTokenUserId = JwtTokenUtil.extractUserId(accessToken);
        if(warehouse.getUserId() != accessTokenUserId) throw new WarehouseInvalidAccessException();

        warehousesRepository.deleteById(warehouseId);
    }

    @Transactional(readOnly = true)
    public WarehouseDetailResponseDto getSpecificWarehouseInfo(Integer warehouseId) {
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);

        return new WarehouseDetailResponseDto(warehouse, noImageUrl);
    }
//
//    private JSONArray createJSONArrayOfWarehouseConditions(Integer warehouseId) {
//        JSONArray jsonArray = ObjectMaker.getJSONArray();
//        List<WarehouseTypesDto> typesDtos = warehouseConditionsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseTypesDto::new).collect(Collectors.toList());
//        for(WarehouseTypesDto dto : typesDtos) {
//            jsonArray.put(dto.getName());
//        }
//        return jsonArray;
//    }

//    private JSONArray createJSONArrayOfWarehouseFacilityUsages(Integer warehouseId) {
//        JSONArray jsonArray = ObjectMaker.getJSONArray();
//        List<WarehouseFacilityUsagesResponseDto> responseDtos = warehouseFacilityUsagesRepository.findByWarehouseId(warehouseId).stream().map(WarehouseFacilityUsagesResponseDto::new).collect(Collectors.toList());
//        for(WarehouseFacilityUsagesResponseDto dto : responseDtos) {
//            jsonArray.put(dto.getContent());
//        }
//        return jsonArray;
//    }
//
//    private JSONArray createJSONArrayOfWarehouseUsageCautions(Integer warehouseId) {
//        JSONArray jsonArray = ObjectMaker.getJSONArray();
//        List<WarehouseUsageCautionsResponseDto> responseDtos = warehouseUsageCautionsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseUsageCautionsResponseDto::new).collect(Collectors.toList());
//        for(WarehouseUsageCautionsResponseDto dto : responseDtos) {
//            jsonArray.put(dto.getContent());
//        }
//        return jsonArray;
//    }
//
//    private JSONObject createJSONObjectOfSpecificWarehouseInfo(Integer warehouseId) throws WarehouseIdNotFoundException {
//        WarehouseResponseDto warehouseResponseDto = new WarehouseResponseDto(warehousesRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new));
//        JSONObject jsonObject = warehouseResponseDto.toJSONObject();
//        WarehouseLocationDto locationDto = new WarehouseLocationDto(warehouseLocationsRepository.findByWarehouseId(warehouseId));
//        jsonObject.put("location", locationDto.toJSONObject());
//        jsonObject.put("warehouseCondition", createJSONArrayOfWarehouseConditions(warehouseId));
//        jsonObject.put("warehouseUsageCautions", createJSONArrayOfWarehouseUsageCautions(warehouseId));
//        jsonObject.put("warehouseFacilityUsages", createJSONArrayOfWarehouseFacilityUsages(warehouseId));
//        Integer agencyWarehouseDetailId = getAgencyWarehouseDetailId(warehouseId);
//        Optional<WarehouseMainImages> imageOptional = warehouseMainImagesRepository.findByWarehouseId(warehouseId);
//        if(imageOptional.isPresent()) {
//            jsonObject.put("mainImageUrl", imageOptional.get().getMainImageUrl());
//        } else {
//            jsonObject.put("mainImageUrl", noImageUrl);
//        }
//        jsonObject.put("images", createJSONArrayOfAttachments(warehouseId));
//        if(warehouseResponseDto.getInsuranceId() != null) {
//            jsonObject.put("insuranceName", insurancesRepository.findByInsuranceId(warehouseResponseDto.getInsuranceId()).getName());
//        }
//        jsonObject.put("agencyDetails", createJSONObjectOfAgencyDetails(warehouseId, agencyWarehouseDetailId));
//        return jsonObject;
//    }
//
//    private JSONArray createJSONArrayOfAttachments(Integer warehouseId) {
//        JSONArray jsonArray = ObjectMaker.getJSONArray();
//        List<WarehouseAttachmentDto> attachmentDtos = warehouseAttachmentsRepository.findByWarehouseId(warehouseId).stream().map(WarehouseAttachmentDto::new).collect(Collectors.toList());
//        for(WarehouseAttachmentDto dto : attachmentDtos) {
//            jsonArray.put(dto.getUrl());
//        }
//        return jsonArray;
//    }
//
//    private Integer getAgencyWarehouseDetailId(Integer warehouseId) throws WarehouseIdNotFoundException{
//        return agencyWarehouseDetailsRepository.findByWarehouseId(warehouseId).orElseThrow(WarehouseIdNotFoundException::new).getAgencyWarehouseDetailId();
//    }
//
//    private JSONArray createJSONArrayOfDeliveryTypes(Integer agencyWarehouseDetailId) {
//        JSONArray jsonArray = ObjectMaker.getJSONArray();
//        List<DeliveryTypes> deliveryTypes = deliveryTypesRepository.findByAgencyWarehouseDetailId(agencyWarehouseDetailId);
//        for(DeliveryTypes type : deliveryTypes) {
//            jsonArray.put(type.getName());
//        }
//        return jsonArray;
//    }
//
//    private JSONObject createJSONObjectOfAgencyDetails(Integer warehouseId, Integer agencyWarehouseDetailId) throws WarehouseIdNotFoundException{
//        JSONObject jsonObject = ObjectMaker.getJSONObject();
//        jsonObject.put("agencyWarehouseDetailId", agencyWarehouseDetailId);
//        Optional<AgencyWarehouseDetails> optionalDetail = agencyWarehouseDetailsRepository.findByWarehouseId(warehouseId);
//        if(!optionalDetail.isPresent()) {
//            throw new WarehouseIdNotFoundException();
//        }
//        AgencyWarehouseDetailResponseDto responseDto = new AgencyWarehouseDetailResponseDto(optionalDetail.get());
//        jsonObject.put("wareohuseType", responseDto.getType());
//        jsonObject.put("minReleasePerMonth", responseDto.getMinReleasePerMonth());
//        jsonObject.put("mainItemType", agencyMainItemTypesRepository.findByAgencyWarehouseDetailId(agencyWarehouseDetailId).getName());
//        jsonObject.put("deliveryTypes", createJSONArrayOfDeliveryTypes(agencyWarehouseDetailId));
//        return jsonObject;
//    }
}

