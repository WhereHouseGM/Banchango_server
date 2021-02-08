package com.banchango.admin.service;

import com.banchango.admin.dto.*;
import com.banchango.admin.exception.WaitingWarehousesNotFoundException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.functions.admin.DoubleCheckAdminAccess;
import com.banchango.common.functions.users.FindUserById;
import com.banchango.common.functions.warehouses.FindWarehouseById;
import com.banchango.domain.deliverytypes.DeliveryType;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.estimateitems.EstimateItem;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.EstimateStatusAndLastModifiedAtAndWarehouseIdProjection;
import com.banchango.domain.estimates.Estimate;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.insurances.Insurance;
import com.banchango.domain.insurances.InsurancesRepository;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.domain.mainitemtypes.MainItemTypesRepository;
import com.banchango.domain.securitycompanies.SecurityCompany;
import com.banchango.domain.securitycompanies.SecurityCompaniesRepository;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.User;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouseconditions.WarehouseCondition;
import com.banchango.domain.warehouseconditions.WarehouseConditionsRepository;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsages;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsagesRepository;
import com.banchango.domain.warehouses.*;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautions;
import com.banchango.domain.warehouseusagecautions.WarehouseUsageCautionsRepository;
import com.banchango.domain.withdraws.WithdrawsRepository;
import com.banchango.estimateitems.dto.EstimateItemSearchDto;
import com.banchango.estimateitems.exception.EstimateItemNotFoundException;
import com.banchango.estimates.exception.EstimateNotFoundException;
import com.banchango.images.dto.ImageInfoResponseDto;
import com.banchango.users.dto.UserInfoResponseDto;
import com.banchango.users.dto.UserSigninRequestDto;
import com.banchango.users.dto.UserSigninResponseDto;
import com.banchango.users.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final WarehousesRepository warehousesRepository;
    private final UsersRepository usersRepository;
    private final MainItemTypesRepository mainItemTypesRepository;
    private final EstimatesRepository estimatesRepository;
    private final WithdrawsRepository withdrawsRepository;
    private final DeliveryTypesRepository deliveryTypesRepository;
    private final SecurityCompaniesRepository securityCompaniesRepository;
    private final WarehouseUsageCautionsRepository warehouseUsageCautionsRepository;
    private final WarehouseFacilityUsagesRepository warehouseFacilityUsagesRepository;
    private final InsurancesRepository insurancesRepository;
    private final WarehouseConditionsRepository warehouseConditionsRepository;

    private final DoubleCheckAdminAccess doubleCheckAdminAccess;
    private final FindWarehouseById findWarehouseById;
    private final FindUserById findUserById;

    @Value("${banchango.no_image.url}")
    private String noImageUrl;

    @Transactional(readOnly = true)
    public WarehouseInsertRequestResponseListDto getWarehouses(String token, PageRequest pageRequest, WarehouseStatus status) {
        doubleCheckAdminAccess.apply(JwtTokenUtil.extractUserId(token));
        List<Warehouses> warehouses;

        if(status == null) warehouses = warehousesRepository.findByOrderByCreatedAtAsc(pageRequest);
        else warehouses  = warehousesRepository.findWarehousesByStatusOrderByCreatedAt(status, pageRequest);

        if(warehouses.isEmpty()) throw new WaitingWarehousesNotFoundException();
        return WarehouseInsertRequestResponseListDto.builder()
                .requests(warehouses.stream().map(WarehouseInsertRequestResponseDto::new).collect(Collectors.toList())).build();
    }

    @Transactional(readOnly = true)
    public WarehouseAdminDetailResponseDto getSpecificWarehouseInfo(String token, Integer warehouseId) {
        doubleCheckAdminAccess.apply(JwtTokenUtil.extractUserId(token));
        Warehouses warehouse = findWarehouseById.apply(warehouseId);
        return new WarehouseAdminDetailResponseDto(warehouse, noImageUrl);
    }

    private void updateInsurances(Warehouses warehouse, WarehouseAdminUpdateRequestDto requestDto) {
        List<Insurance> insurances = insurancesRepository.findByWarehouseId(warehouse.getId());
        if(insurances.size() == requestDto.getInsurances().size()) {
            for(int i = 0; i < insurances.size(); i++) {
                insurances.get(i).setName(requestDto.getInsurances().get(i));
            }
        }
        else if(insurances.size() < requestDto.getInsurances().size()) {
            for(int i = 0; i < insurances.size(); i++) {
                insurances.get(i).setName(requestDto.getInsurances().get(i));
            }
            for(int i = insurances.size(); i < requestDto.getInsurances().size(); i++) {
                Insurance newInsurance = Insurance.builder()
                        .warehouse(warehouse).name(requestDto.getInsurances().get(i))
                        .build();
                insurancesRepository.save(newInsurance);
            }
        }
        else if(insurances.size() > requestDto.getInsurances().size()) {
            for(int i = 0; i < requestDto.getInsurances().size(); i++) {
                insurances.get(i).setName(requestDto.getInsurances().get(i));
            }
            for(int i = requestDto.getInsurances().size(); i < insurances.size(); i++) {
                Integer idOfInsuranceToRemove = insurances.get(i).getId();
                warehouse.getInsurances().removeIf(insurance -> insurance.getId().equals(idOfInsuranceToRemove));
                insurancesRepository.deleteById(idOfInsuranceToRemove);
            }
        }
    }

    private void updateSecurityCompanies(Warehouses warehouse, WarehouseAdminUpdateRequestDto requestDto) {
        List<SecurityCompany> securityCompanies = securityCompaniesRepository.findByWarehouseId(warehouse.getId());
        if(securityCompanies.size() == requestDto.getSecurityCompanies().size()) {
            for(int i = 0; i < securityCompanies.size(); i++) {
                securityCompanies.get(i).setName(requestDto.getSecurityCompanies().get(i));
            }
        }
        else if(securityCompanies.size() < requestDto.getSecurityCompanies().size()) {
            for(int i = 0; i < securityCompanies.size(); i++) {
                securityCompanies.get(i).setName(requestDto.getSecurityCompanies().get(i));
            }
            for(int i = securityCompanies.size(); i < requestDto.getSecurityCompanies().size(); i++) {
                SecurityCompany newSecurityCompany = SecurityCompany.builder()
                        .warehouse(warehouse).name(requestDto.getSecurityCompanies().get(i))
                        .build();
                securityCompaniesRepository.save(newSecurityCompany);
            }
        }
        else if(securityCompanies.size() > requestDto.getSecurityCompanies().size()) {
            for (int i = 0; i < requestDto.getSecurityCompanies().size(); i++) {
                securityCompanies.get(i).setName(requestDto.getSecurityCompanies().get(i));
            }
            for (int i = requestDto.getSecurityCompanies().size(); i < securityCompanies.size(); i++) {
                Integer idOfCompanyToRemove = securityCompanies.get(i).getId();
                warehouse.getSecurityCompanies().removeIf(company -> company.getId().equals(idOfCompanyToRemove));
                securityCompaniesRepository.deleteById(idOfCompanyToRemove);
            }
        }
    }

    private void updateDeliveryTypes(Warehouses warehouse, WarehouseAdminUpdateRequestDto requestDto) {
        List<DeliveryType> deliveryTypes = deliveryTypesRepository.findByWarehouseId(warehouse.getId());
        if(deliveryTypes.size() == requestDto.getDeliveryTypes().size()) {
            for(int i = 0; i < deliveryTypes.size(); i++) {
                deliveryTypes.get(i).setName(requestDto.getDeliveryTypes().get(i));
            }
        }
        else if(deliveryTypes.size() < requestDto.getDeliveryTypes().size()) {
            for(int i = 0; i < deliveryTypes.size(); i++) {
                deliveryTypes.get(i).setName(requestDto.getDeliveryTypes().get(i));
            }
            for(int i = deliveryTypes.size(); i < requestDto.getDeliveryTypes().size(); i++) {
                DeliveryType newDeliveryType = DeliveryType.builder()
                        .warehouse(warehouse).name(requestDto.getDeliveryTypes().get(i))
                        .build();
                deliveryTypesRepository.save(newDeliveryType);
            }
        }
        else if(deliveryTypes.size() > requestDto.getDeliveryTypes().size()) {
            for(int i = 0; i < requestDto.getDeliveryTypes().size(); i++) {
                deliveryTypes.get(i).setName(requestDto.getDeliveryTypes().get(i));
            }
            for(int i = requestDto.getDeliveryTypes().size(); i < deliveryTypes.size(); i++) {
                Integer idOfDeliveryTypeToRemove = deliveryTypes.get(i).getId();
                warehouse.getDeliveryTypes().removeIf(type -> type.getId().equals(idOfDeliveryTypeToRemove));
                deliveryTypesRepository.deleteById(idOfDeliveryTypeToRemove);
            }
        }
    }

    private void updateWarehouseConditions(Warehouses warehouse, WarehouseAdminUpdateRequestDto requestDto) {
        List<WarehouseCondition> warehouseConditions = warehouseConditionsRepository.findByWarehouseId(warehouse.getId());
        if(warehouseConditions.size() == requestDto.getWarehouseCondition().size()) {
            for(int i = 0; i < warehouseConditions.size(); i++) {
                warehouseConditions.get(i).setCondition(requestDto.getWarehouseCondition().get(i));
            }
        }
        else if(warehouseConditions.size() < requestDto.getWarehouseCondition().size()) {
            for(int i = 0; i < warehouseConditions.size(); i++) {
                warehouseConditions.get(i).setCondition(requestDto.getWarehouseCondition().get(i));
            }
            for(int i = warehouseConditions.size(); i < requestDto.getWarehouseCondition().size(); i++) {
                WarehouseCondition newCondition = WarehouseCondition.builder()
                        .warehouse(warehouse).condition(requestDto.getWarehouseCondition().get(i))
                        .build();
                warehouseConditionsRepository.save(newCondition);
            }
        }
        else if(warehouseConditions.size() > requestDto.getWarehouseCondition().size()) {
            for(int i = 0; i < requestDto.getWarehouseCondition().size(); i++) {
                warehouseConditions.get(i).setCondition(requestDto.getWarehouseCondition().get(i));
            }
            for(int i = requestDto.getWarehouseCondition().size(); i < warehouseConditions.size(); i++) {
                Integer idOfConditionToRemove = warehouseConditions.get(i).getId();
                warehouse.getWarehouseConditions().removeIf(condition -> condition.getId().equals(idOfConditionToRemove));
                warehouseConditionsRepository.deleteById(idOfConditionToRemove);
            }
        }
    }

    private void updateWarehouseFacilityUsages(Warehouses warehouse, WarehouseAdminUpdateRequestDto requestDto) {
        List<WarehouseFacilityUsages> warehouseFacilityUsages = warehouseFacilityUsagesRepository.findByWarehouseId(warehouse.getId());
        if(warehouseFacilityUsages.size() == requestDto.getWarehouseFacilityUsages().size()) {
            for(int i = 0; i < warehouseFacilityUsages.size(); i++) {
                warehouseFacilityUsages.get(i).setContent(requestDto.getWarehouseFacilityUsages().get(i));
            }
        }
        else if(warehouseFacilityUsages.size() < requestDto.getWarehouseFacilityUsages().size()) {
            for(int i = 0; i < warehouseFacilityUsages.size(); i++) {
                warehouseFacilityUsages.get(i).setContent(requestDto.getWarehouseFacilityUsages().get(i));
            }
            for(int i = warehouseFacilityUsages.size(); i < requestDto.getWarehouseFacilityUsages().size(); i++) {
                WarehouseFacilityUsages newUsage = WarehouseFacilityUsages.builder()
                        .warehouse(warehouse).content(requestDto.getWarehouseFacilityUsages().get(i))
                        .build();
                warehouseFacilityUsagesRepository.save(newUsage);
            }
        }
        else if(warehouseFacilityUsages.size() > requestDto.getWarehouseFacilityUsages().size()) {
            for(int i = 0; i < requestDto.getWarehouseFacilityUsages().size(); i++) {
                warehouseFacilityUsages.get(i).setContent(requestDto.getWarehouseFacilityUsages().get(i));
            }
            for(int i = requestDto.getWarehouseFacilityUsages().size(); i < warehouseFacilityUsages.size(); i++) {
                Integer idOfUsageToRemove = warehouseFacilityUsages.get(i).getId();
                warehouse.getWarehouseFacilityUsages().removeIf(usage -> usage.getId().equals(idOfUsageToRemove));
                warehouseFacilityUsagesRepository.deleteById(idOfUsageToRemove);
            }
        }
    }

    private void updateWarehouseUsageCautions(Warehouses warehouse, WarehouseAdminUpdateRequestDto requestDto) {
        List<WarehouseUsageCautions> warehouseUsageCautions = warehouseUsageCautionsRepository.findByWarehouseId(warehouse.getId());
        if(warehouseUsageCautions.size() == requestDto.getWarehouseUsageCautions().size()) {
            for(int i = 0; i < warehouseUsageCautions.size(); i++) {
                warehouseUsageCautions.get(i).setContent(requestDto.getWarehouseUsageCautions().get(i));
            }
        }
        else if(warehouseUsageCautions.size() < requestDto.getWarehouseUsageCautions().size()) {
            for(int i = 0; i < warehouseUsageCautions.size(); i++) {
                warehouseUsageCautions.get(i).setContent(requestDto.getWarehouseUsageCautions().get(i));
            }
            for(int i = warehouseUsageCautions.size(); i < requestDto.getWarehouseUsageCautions().size(); i++) {
                WarehouseUsageCautions newCaution = WarehouseUsageCautions.builder()
                        .warehouse(warehouse).content(requestDto.getWarehouseUsageCautions().get(i))
                        .build();
                warehouseUsageCautionsRepository.save(newCaution);
            }
        }
        else if(warehouseUsageCautions.size() > requestDto.getWarehouseUsageCautions().size()) {
            for(int i = 0; i < requestDto.getWarehouseUsageCautions().size(); i++) {
                warehouseUsageCautions.get(i).setContent(requestDto.getWarehouseUsageCautions().get(i));
            }
            for(int i = requestDto.getWarehouseUsageCautions().size(); i < warehouseUsageCautions.size(); i++) {
                Integer idOfCautionToRemove = warehouseUsageCautions.get(i).getId();
                warehouse.getWarehouseUsageCautions().removeIf(caution -> caution.getId().equals(idOfCautionToRemove));
                warehouseUsageCautionsRepository.deleteById(idOfCautionToRemove);
            }
        }
    }

    private void updateMainItemTypes(Warehouses warehouse, WarehouseAdminUpdateRequestDto requestDto) {
        List<MainItemType> mainItemTypes = mainItemTypesRepository.findByWarehouseId(warehouse.getId());
        if(mainItemTypes.size() == requestDto.getMainItemTypes().size()) {
            for(int i = 0; i < mainItemTypes.size(); i++) {
                mainItemTypes.get(i).setType(requestDto.getMainItemTypes().get(i));
            }
        }
        else if(mainItemTypes.size() < requestDto.getMainItemTypes().size()) {
            for(int i = 0; i < mainItemTypes.size(); i++) {
                mainItemTypes.get(i).setType(requestDto.getMainItemTypes().get(i));
            }
            for(int i = mainItemTypes.size(); i < requestDto.getMainItemTypes().size(); i++) {
                MainItemType newType = MainItemType.builder()
                        .warehouse(warehouse).mainItemType(requestDto.getMainItemTypes().get(i))
                        .build();
                mainItemTypesRepository.save(newType);
            }
        }
        else if(mainItemTypes.size() > requestDto.getMainItemTypes().size()) {
            for(int i = 0; i < requestDto.getMainItemTypes().size(); i++) {
                mainItemTypes.get(i).setType(requestDto.getMainItemTypes().get(i));
            }
            for(int i = requestDto.getMainItemTypes().size(); i < mainItemTypes.size(); i++) {
                Integer idOfTypeToRemove = mainItemTypes.get(i).getId();
                warehouse.getMainItemTypes().removeIf(type -> type.getId().equals(idOfTypeToRemove));
                mainItemTypesRepository.deleteById(idOfTypeToRemove);
            }
        }
    }

    @Transactional
    public WarehouseAdminDetailResponseDto updateWarehouse(WarehouseAdminUpdateRequestDto requestDto, String token, Integer warehouseId) {
        doubleCheckAdminAccess.apply(JwtTokenUtil.extractUserId(token));
        Warehouses warehouse = findWarehouseById.apply(warehouseId);
        updateInsurances(warehouse, requestDto);
        updateSecurityCompanies(warehouse, requestDto);
        updateDeliveryTypes(warehouse, requestDto);
        updateWarehouseConditions(warehouse, requestDto);
        updateWarehouseFacilityUsages(warehouse, requestDto);
        updateWarehouseUsageCautions(warehouse, requestDto);
        updateMainItemTypes(warehouse, requestDto);
        warehouse.update(requestDto);
        return new WarehouseAdminDetailResponseDto(warehouse, noImageUrl);
    }

    @Transactional(readOnly = true)
    public List<EstimateSummaryDto> getEstimates(String token, EstimateStatus status, PageRequest pageRequest) {
        doubleCheckAdminAccess.apply(JwtTokenUtil.extractUserId(token));
        List<EstimateStatusAndLastModifiedAtAndWarehouseIdProjection> estimates;
        if (status == null) estimates = estimatesRepository.findByOrderByIdAsc(pageRequest, EstimateStatusAndLastModifiedAtAndWarehouseIdProjection.class);
        else estimates = estimatesRepository.findByStatusOrderByIdAsc(status, pageRequest, EstimateStatusAndLastModifiedAtAndWarehouseIdProjection.class);

        if(estimates.isEmpty()) throw new EstimateNotFoundException();

        return estimates.stream()
                .map(estimate -> {
                    EstimateSummaryDto estimateSummaryDto = new EstimateSummaryDto(estimate);
                    Optional<WarehouseIdAndNameProjection> optionalProjection = warehousesRepository.findByIdAndStatus(estimate.getWarehouseId(), WarehouseStatus.VIEWABLE, WarehouseIdAndNameProjection.class);
                    if(optionalProjection.isPresent()) {
                        WarehouseIdAndNameProjection projection = optionalProjection.get();
                        estimateSummaryDto.updateWarehouseInfo(projection);
                    }
                    return estimateSummaryDto;
                })
                .filter(dto -> dto.getName() != null && dto.getWarehouseId() != null)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateEstimateStatus(String token, Integer estimateId, EstimateStatusUpdateRequestDto estimateStatusUpdateRequestDto) {
        doubleCheckAdminAccess.apply(JwtTokenUtil.extractUserId(token));
        Estimate estimate = estimatesRepository.findById(estimateId).orElseThrow(EstimateNotFoundException::new);
        estimate.updateStatus(estimateStatusUpdateRequestDto.getStatus());
    }

    @Transactional(readOnly = true)
    public List<EstimateItemSearchDto> getEstimateItems(String token, Integer estimateId) {
        doubleCheckAdminAccess.apply(JwtTokenUtil.extractUserId(token));
        Estimate estimate = estimatesRepository.findById(estimateId).orElseThrow(EstimateNotFoundException::new);
        List<EstimateItem> estimateItems = estimate.getEstimateItems();
        if(estimateItems.size() == 0) throw new EstimateItemNotFoundException();
        return estimate.getEstimateItems().stream()
            .map(EstimateItemSearchDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstimateDetailResponseDto getEstimate(String token, Integer estimateId) {
        doubleCheckAdminAccess.apply(JwtTokenUtil.extractUserId(token));
        Estimate estimate = estimatesRepository.findById(estimateId).orElseThrow(EstimateNotFoundException::new);
        User user = findUserById.apply(estimate.getUserId());
        Optional<WarehouseNameProjection> optionalWarehouseNameProjection = warehousesRepository.findById(estimate.getWarehouseId(), WarehouseNameProjection.class);
        String warehouseName;
        boolean isUserDeleted = withdrawsRepository.findByUserId(user.getUserId()).isPresent();

        if (optionalWarehouseNameProjection.isPresent())
            warehouseName = optionalWarehouseNameProjection.get().getName();
        else warehouseName = "삭제된 창고";

        return EstimateDetailResponseDto.builder()
            .estimate(estimate)
            .user(user)
            .warehouseName(warehouseName)
            .isDeleted(isUserDeleted)
            .build();
    }

    @Transactional(readOnly = true)
    public UserSigninResponseDto signIn(UserSigninRequestDto requestDto) {
        UserSigninResponseDto responseDto = new UserSigninResponseDto();
        User user = usersRepository.findByEmailAndPasswordAndRole(requestDto.getEmail(), requestDto.getPassword(), UserRole.ADMIN).orElseThrow(UserNotFoundException::new);

        boolean isUserDeleted = withdrawsRepository.findByUserId(user.getUserId()).isPresent();

        UserInfoResponseDto userInfoDto = new UserInfoResponseDto(user, isUserDeleted);
        responseDto.setAccessToken(JwtTokenUtil.generateAccessToken(userInfoDto.getUserId(), userInfoDto.getRole(), userInfoDto.getType()));
        responseDto.setRefreshToken(JwtTokenUtil.generateRefreshToken(userInfoDto.getUserId(), userInfoDto.getRole(), userInfoDto.getType()));
        responseDto.setTokenType("Bearer");
        responseDto.setUser(userInfoDto);
        return responseDto;
    }

    @Transactional(readOnly = true)
    public ImagesAdminResponseDto getImages(String token, Integer warehouseId) {
        doubleCheckAdminAccess.apply(JwtTokenUtil.extractUserId(token));
        Warehouses warehouse = findWarehouseById.apply(warehouseId);
        List<ImageInfoResponseDto> images = warehouse.getWarehouseImages()
            .stream().map(ImageInfoResponseDto::new).collect(Collectors.toList());
        ImagesAdminResponseDto responseDto = new ImagesAdminResponseDto();
        responseDto.setImages(images);
        responseDto.setWarehouseName(warehouse.getName());
        return responseDto;
    }
}
