package com.banchango.warehouses.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.service.EmailSender;
import com.banchango.domain.deliverytypes.DeliveryTypes;
import com.banchango.domain.insurances.Insurances;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypes;
import com.banchango.domain.mainitemtypes.MainItemTypesRepository;
import com.banchango.domain.securitycompanies.SecurityCompanies;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouseconditions.WarehouseConditions;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import com.banchango.users.exception.ForbiddenUserIdException;
import com.banchango.users.exception.UserIdNotFoundException;
import com.banchango.warehouses.dto.*;
import com.banchango.warehouses.exception.*;
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
    private final UsersRepository usersRepository;
    private final EmailSender emailSender;
    private final MainItemTypesRepository mainItemTypesRepository;

    @Value("${banchango.no_image.url}")
    private String noImageUrl;

    @Transactional
    public BasicMessageResponseDto saveAgencyWarehouse(WarehouseInsertRequestDto warehouseInsertRequestDto, String accessToken) {
        int userId = JwtTokenUtil.extractUserId(accessToken);
        Users user = usersRepository.findById(userId).orElseThrow(UserIdNotFoundException::new);


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
                .cctvExist(warehouseInsertRequestDto.getCctvExist())
                .doorLockExist(warehouseInsertRequestDto.getDoorLockExist())
                .airConditioningType(warehouseInsertRequestDto.getAirConditioningType())
                .workerExist(warehouseInsertRequestDto.getWorkerExist())
                .canPark(warehouseInsertRequestDto.getCanPark())
                .warehouseType(warehouseInsertRequestDto.getWarehouseType())
                .minReleasePerMonth(warehouseInsertRequestDto.getMinReleasePerMonth())
                .latitude(warehouseInsertRequestDto.getLatitude())
                .longitude(warehouseInsertRequestDto.getLongitude())
                .status(WarehouseStatus.IN_PROGRESS)
                .build();

        final Warehouses savedWarehouse = warehousesRepository.save(warehouse);

        List<MainItemTypes> mainItemTypes = warehouseInsertRequestDto.getMainItemTypes().stream()
            .map(type -> new MainItemTypes(type, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setMainItemTypes(mainItemTypes);

        List<DeliveryTypes> deliveryTypes = warehouseInsertRequestDto.getDeliveryTypes().stream()
                .map(type -> new DeliveryTypes(type, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setDeliveryTypes(deliveryTypes);

        List<WarehouseConditions> warehouseConditions = warehouseInsertRequestDto.getWarehouseCondition().stream()
                .map(type -> new WarehouseConditions(type, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setWarehouseConditions(warehouseConditions);

        List<WarehouseFacilityUsages> warehouseFacilityUsages = warehouseInsertRequestDto.getWarehouseFacilityUsages().stream()
                .map(usage -> new WarehouseFacilityUsages(usage, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setWarehouseFacilityUsages(warehouseFacilityUsages);

        List<WarehouseUsageCautions> warehouseUsageCautions = warehouseInsertRequestDto.getWarehouseUsageCautions().stream()
                .map(caution -> new WarehouseUsageCautions(caution, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setWarehouseUsageCautions(warehouseUsageCautions);

        List<Insurances> insurances = warehouseInsertRequestDto.getInsurances().stream()
                .map(insurance -> new Insurances(insurance, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setInsurances(insurances);

        List<SecurityCompanies> securityCompanies = warehouseInsertRequestDto.getSecurityCompanies().stream()
                .map(company -> new SecurityCompanies(company, savedWarehouse)).collect(Collectors.toList());
        savedWarehouse.setSecurityCompanies(securityCompanies);

        EmailContent emailContent = new EmailContent("[반창고] 창고 등록 요청 안내", "안녕하세요, 반창고 입니다!", "<span style='font-size: 20px'>" + warehouseInsertRequestDto.getName() + "</span>에 대한 창고 등록 요청이 완료되었으며, 영업 팀의 인증 절차 후 등록이 완료될 예정입니다.", "문의사항은 wherehousegm@gmail.com으로 답변 주세요.", "반창고", "dev.banchangohub.com");
        return emailSender.send(user.getEmail(), emailContent, true);
    }

    @Transactional(readOnly = true)
    public List<WarehouseSearchDto> getWarehousesByAddress(String address, PageRequest pageRequest) {
        List<WarehouseSearchDto> warehouses = warehousesRepository.findByAddressContainingAndStatus(address, WarehouseStatus.VIEWABLE, pageRequest)
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

        if(warehouses.size() == 0) throw new WarehouseNotFoundException("해당 카테고리로 등록된 창고 결과가 존재하지 않습니다");

        return warehouses;
    }

    @Transactional(readOnly = true)
    public List<WarehouseSearchDto> getWarehouses(PageRequest pageRequest) {
        List<WarehouseSearchDto> warehouses = warehousesRepository.findAllByStatus(WarehouseStatus.VIEWABLE, pageRequest)
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
        if(warehouse.getStatus().equals(WarehouseStatus.DELETED)) throw new WarehouseNotFoundException();

        warehousesRepository.deleteById(warehouseId);
    }

    @Transactional(readOnly = true)
    public WarehouseDetailResponseDto getSpecificWarehouseInfo(Integer warehouseId) {
        Warehouses warehouse = warehousesRepository.findByIdAndStatus(warehouseId, WarehouseStatus.VIEWABLE).orElseThrow(WarehouseIdNotFoundException::new);

        return new WarehouseDetailResponseDto(warehouse, noImageUrl);
    }

    @Transactional
    public WarehouseDetailResponseDto updateWarehouse(String accessToken, Integer warehouseId, WarehouseUpdateRequestDto requestDto) {
        int userId = JwtTokenUtil.extractUserId(accessToken);

        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        if(warehouse.getStatus().equals(WarehouseStatus.DELETED)) throw new WarehouseNotFoundException();
        if(!warehouse.getStatus().equals(WarehouseStatus.VIEWABLE)) throw new WarehouseIsNotViewableException();

        if(!warehouse.getUserId().equals(userId)) throw new ForbiddenUserIdException();

        if(!mainItemTypesRepository.findByWarehouseId(warehouseId).equals(requestDto.getMainItemTypes())) {
            mainItemTypesRepository.deleteByWarehouseId(warehouseId);
        }

        warehouse.update(requestDto);
        return new WarehouseDetailResponseDto(warehouse, noImageUrl);
    }

    @Transactional(readOnly = true)
    public List<MyWarehouseDto> getMyWarehouses(String accessToken, Integer userId) {
        int userIdFromAccessToken = JwtTokenUtil.extractUserId(accessToken);
        if(!userId.equals(userIdFromAccessToken)) throw new ForbiddenUserIdException("해당 사용자의 창고 목록을 볼 수 있는 권한이 없습니다");
        Users user = usersRepository.findById(userId).orElseThrow(UserIdNotFoundException::new);

        List<MyWarehouseDto> warehouses = warehousesRepository.findByUserId(userId).stream()
            .filter(warehouse -> !warehouse.getStatus().equals(WarehouseStatus.DELETED))
            .map(warehouse -> new MyWarehouseDto(warehouse, noImageUrl))
            .collect(Collectors.toList());

        if(warehouses.isEmpty()) throw new WarehouseNotFoundException();

        return warehouses;
    }
}

