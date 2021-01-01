package com.banchango.warehouses.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import com.banchango.warehouses.dto.WarehouseInsertRequestDto;
import com.banchango.warehouses.dto.WarehouseSearchDto;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import com.banchango.warehouses.exception.WarehouseInvalidAccessException;
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

    @Value("${banchango.no_image.url}")
    private String noImageUrl;

    @Transactional
    public void saveAgencyWarehouse(WarehouseInsertRequestDto warehouseInsertRequestDto, String accessToken) {
        int userId = JwtTokenUtil.extractUserId(accessToken);

        Warehouses warehouse = Warehouses.builder()
                .userId(userId)
                .name(warehouseInsertRequestDto.getName())
                .space(warehouseInsertRequestDto.getSpace())
                .address(warehouseInsertRequestDto.getAddress())
                .addressDetail(warehouseInsertRequestDto.getAddressDetail())
                .description(warehouseInsertRequestDto.getDescription())
                .availableWeekdays(warehouseInsertRequestDto.getAvailableWeekdays())
                .openAt(warehouseInsertRequestDto.getOpenAt())
                .closeAt(warehouseInsertRequestDto.getCloseAt())
                .availableTimeDetail(warehouseInsertRequestDto.getAvailableTimeDetail())
                .insurance(warehouseInsertRequestDto.getInsurance())
                .cctvExist(warehouseInsertRequestDto.getCctvExist())
                .securityCompanyName(warehouseInsertRequestDto.getSecurityCompanyName())
                .doorLockExist(warehouseInsertRequestDto.getDoorLockExist())
                .airConditioningType(warehouseInsertRequestDto.getAirConditioningType())
                .workerExist(warehouseInsertRequestDto.getWorkerExist())
                .canPark(warehouseInsertRequestDto.getCanPark())
                .warehouseType(warehouseInsertRequestDto.getWarehouseType())
                .minReleasePerMonth(warehouseInsertRequestDto.getMinReleasePerMonth())
                .latitude(warehouseInsertRequestDto.getLatitude())
                .longitude(warehouseInsertRequestDto.getLongitude())
                .isViewableFlag(false)
                .build();

        final Warehouses savedWarehouse = warehousesRepository.save(warehouse);

        List<MainItemTypes> mainItemTypes = warehouseInsertRequestDto.getMainItemTypes().stream()
            .map((type) -> new MainItemTypes(type, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setMainItemTypes(mainItemTypes);

        List<DeliveryTypes> deliveryTypes = warehouseInsertRequestDto.getDeliveryTypes().stream()
                .map((type) -> new DeliveryTypes(type, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setDeliveryTypes(deliveryTypes);

        List<WarehouseConditions> warehouseConditions = warehouseInsertRequestDto.getWarehouseCondition().stream()
                .map((type) -> new WarehouseConditions(type, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setWarehouseConditions(warehouseConditions);

        List<WarehouseFacilityUsages> warehouseFacilityUsages = warehouseInsertRequestDto.getWarehouseFacilityUsages().stream()
                .map((usage) -> new WarehouseFacilityUsages(usage, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setWarehouseFacilityUsages(warehouseFacilityUsages);

        List<WarehouseUsageCautions> warehouseUsageCautions = warehouseInsertRequestDto.getWarehouseUsageCautions().stream()
                .map((caution) -> new WarehouseUsageCautions(caution, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setWarehouseUsageCautions(warehouseUsageCautions);
    }

    @Transactional(readOnly = true)
    public List<WarehouseSearchDto> getWarehousesByAddress(String address, PageRequest pageRequest) {
        List<WarehouseSearchDto> warehouses = warehousesRepository.findByAddressContainingAndIsViewableFlag(address, true, pageRequest)
                .stream()
                .map(warehouse -> new WarehouseSearchDto(warehouse, noImageUrl))
                .collect(Collectors.toList());

        if(warehouses.size() == 0) throw new WarehouseSearchException();

        return warehouses;
    }

    @Transactional(readOnly = true)
    public List<WarehouseSearchDto> getWarehousesByMainItemTypes(List<MainItemType> mainItemTypes, PageRequest pageRequest) {
        List<WarehouseSearchDto> warehouses = warehousesRepository.findViewableWarehouseByMainItemTypes(mainItemTypes, pageRequest)
            .stream()
            .map(warehouse -> new WarehouseSearchDto(warehouse, noImageUrl, mainItemTypes))
            .collect(Collectors.toList());

        if(warehouses.size() == 0) throw new WarehouseNotFoundException();

        return warehouses;
    }

    @Transactional(readOnly = true)
    public List<WarehouseSearchDto> getWarehouses(PageRequest pageRequest) {
        List<WarehouseSearchDto> warehouses = warehousesRepository.findAllByIsViewableFlag(true, pageRequest)
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
        Warehouses warehouse = warehousesRepository.findByIdAndIsViewableFlag(warehouseId, true).orElseThrow(WarehouseIdNotFoundException::new);

        return new WarehouseDetailResponseDto(warehouse, noImageUrl);
    }
}

