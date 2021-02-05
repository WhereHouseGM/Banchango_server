package com.banchango.admin.service;

import com.banchango.admin.dto.*;
import com.banchango.admin.exception.AdminInvalidAccessException;
import com.banchango.admin.exception.WaitingWarehousesNotFoundException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.deliverytypes.DeliveryTypesRepository;
import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.EstimateStatusAndLastModifiedAtAndWarehouseIdProjection;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.insurances.Insurances;
import com.banchango.domain.insurances.InsurancesRepository;
import com.banchango.domain.mainitemtypes.MainItemTypesRepository;
import com.banchango.domain.securitycompanies.SecurityCompanies;
import com.banchango.domain.securitycompanies.SecurityCompaniesRepository;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouseconditions.WarehouseConditionsRepository;
import com.banchango.domain.warehousefacilityusages.WarehouseFacilityUsagesRepository;
import com.banchango.domain.warehouses.*;
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
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import com.banchango.warehouses.exception.WarehouseImageNotFoundException;
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

    @Value("${banchango.no_image.url}")
    private String noImageUrl;

    private void doubleCheckAdminAccess(Integer userId) {
        Users user = usersRepository.findById(userId).orElseThrow(AdminInvalidAccessException::new);
        if(!user.getRole().equals(UserRole.ADMIN)) throw new AdminInvalidAccessException();
    }

    @Transactional(readOnly = true)
    public WarehouseInsertRequestResponseListDto getWarehouses(String token, PageRequest pageRequest, WarehouseStatus status) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(token));
        List<Warehouses> warehouses;

        if(status == null) warehouses = warehousesRepository.findByOrderByCreatedAtAsc(pageRequest);
        else warehouses  = warehousesRepository.findWarehousesByStatusOrderByCreatedAt(status, pageRequest);

        if(warehouses.isEmpty()) throw new WaitingWarehousesNotFoundException();
        return WarehouseInsertRequestResponseListDto.builder()
                .requests(warehouses.stream().map(WarehouseInsertRequestResponseDto::new).collect(Collectors.toList())).build();
    }

    @Transactional(readOnly = true)
    public WarehouseAdminDetailResponseDto getSpecificWarehouseInfo(String token, Integer warehouseId) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(token));
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        return new WarehouseAdminDetailResponseDto(warehouse, noImageUrl);
    }

    private void updateInsurances(Warehouses warehouse, WarehouseAdminUpdateRequestDto requestDto) {
        List<Insurances> insurances = insurancesRepository.findByWarehouseId(warehouse.getId());
        // 기존 개수와 같다면
        if(insurances.size() == requestDto.getInsurances().size()) {
            for(int i = 0; i < insurances.size(); i++) {
                insurances.get(i).setName(requestDto.getInsurances().get(i));
            }
        }

        // 기존 개수보다 많다면
        else if(insurances.size() < requestDto.getInsurances().size()) {
            for(int i = 0; i < insurances.size(); i++) {
                insurances.get(i).setName(requestDto.getInsurances().get(i));
            }
            for(int i = insurances.size() + 1; i < requestDto.getInsurances().size(); i++) {
                Insurances newInsurance = Insurances.builder()
                        .warehouse(warehouse).name(requestDto.getInsurances().get(i))
                        .build();
                insurancesRepository.save(newInsurance);
            }
        }

        // 기존 개수보다 적다면
        else if(insurances.size() > requestDto.getInsurances().size()) {
            for(int i = 0; i < requestDto.getInsurances().size(); i++) {
                insurances.get(i).setName(requestDto.getInsurances().get(i));
            }
            for(int i = requestDto.getInsurances().size() + 1; i < insurances.size(); i++) {
                insurancesRepository.delete(insurances.get(i));
            }
        }
    }

    private void updateSecurityCompanies(Warehouses warehouse, WarehouseAdminUpdateRequestDto requestDto) {
        List<SecurityCompanies> securityCompanies = securityCompaniesRepository.findByWarehouseId(warehouse.getId());
        if(securityCompanies.size() == requestDto.getSecurityCompanies().size()) {
            for(int i = 0; i < securityCompanies.size(); i++) {
                securityCompanies.get(i).setName(requestDto.getSecurityCompanies().get(i));
            }
        }
        else if(securityCompanies.size() < requestDto.getSecurityCompanies().size()) {
            for(int i = 0; i < securityCompanies.size(); i++) {
                securityCompanies.get(i).setName(requestDto.getSecurityCompanies().get(i));
            }
            for(int i = securityCompanies.size() + 1; i < requestDto.getSecurityCompanies().size(); i++) {
                SecurityCompanies newSecurityCompany = SecurityCompanies.builder()
                        .warehouse(warehouse).name(requestDto.getSecurityCompanies().get(i))
                        .build();
                securityCompaniesRepository.save(newSecurityCompany);
            }
        }
        else if(securityCompanies.size() > requestDto.getSecurityCompanies().size()) {
            for(int i = 0; i < requestDto.getSecurityCompanies().size(); i++) {
                securityCompanies.get(i).setName(requestDto.getSecurityCompanies().get(i));
            }
            for(int i = requestDto.getSecurityCompanies().size() + 1; i < securityCompanies.size(); i++) {
                securityCompaniesRepository.delete(securityCompanies.get(i));
            }
        }
    }

    @Transactional
    public WarehouseAdminDetailResponseDto updateWarehouse(WarehouseAdminUpdateRequestDto requestDto, String token, Integer warehouseId) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(token));
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        // TODO : 로직 개선
        updateInsurances(warehouse, requestDto);
        updateSecurityCompanies(warehouse, requestDto);

        // TODO : 주석 제거
//        deliveryTypesRepository.deleteByWarehouseId(warehouseId);
//        securityCompaniesRepository.deleteByWarehouseId(warehouseId);
//        warehouseFacilityUsagesRepository.deleteByWarehouseId(warehouseId);
//        warehouseUsageCautionsRepository.deleteByWarehouseId(warehouseId);
//        insurancesRepository.deleteByWarehouseId(warehouseId);
//        mainItemTypesRepository.deleteByWarehouseId(warehouseId);
//        warehouseConditionsRepository.deleteByWarehouseId(warehouseId);
//        if(!mainItemTypesRepository.findByWarehouseId(warehouseId).stream().map(MainItemTypes::getType).collect(Collectors.toList()).equals(requestDto.getMainItemTypes())) {
//            mainItemTypesRepository.deleteByWarehouseId(warehouseId);
//        }
        warehouse.update(requestDto);
        return new WarehouseAdminDetailResponseDto(warehouse, noImageUrl);
    }

    @Transactional(readOnly = true)
    public List<EstimateSummaryDto> getEstimates(String accessToken, EstimateStatus status, PageRequest pageRequest) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(accessToken));
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
    public void updateEstimateStatus(String accessToken, Integer estimateId, EstimateStatusUpdateRequestDto estimateStatusUpdateRequestDto) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(accessToken));
        Estimates estimate = estimatesRepository.findById(estimateId).orElseThrow(EstimateNotFoundException::new);
        estimate.updateStatus(estimateStatusUpdateRequestDto.getStatus());
    }

    @Transactional(readOnly = true)
    public List<EstimateItemSearchDto> getEstimateItems(String accessToken, Integer estimateId) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(accessToken));
        Estimates estimate = estimatesRepository.findById(estimateId).orElseThrow(EstimateNotFoundException::new);
        List<EstimateItems> estimateItems = estimate.getEstimateItems();
        if(estimateItems.size() == 0) throw new EstimateItemNotFoundException();
        return estimate.getEstimateItems().stream()
            .map(EstimateItemSearchDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstimateDetailResponseDto getEstimate(String accessToken, Integer estimateId) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(accessToken));
        Estimates estimate = estimatesRepository.findById(estimateId).orElseThrow(EstimateNotFoundException::new);
        Users user = usersRepository.findById(estimate.getUserId()).get();
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
        Users user = usersRepository.findByEmailAndPasswordAndRole(requestDto.getEmail(), requestDto.getPassword(), UserRole.ADMIN).orElseThrow(UserNotFoundException::new);

        boolean isUserDeleted = withdrawsRepository.findByUserId(user.getUserId()).isPresent();

        UserInfoResponseDto userInfoDto = new UserInfoResponseDto(user, isUserDeleted);
        responseDto.setAccessToken(JwtTokenUtil.generateAccessToken(userInfoDto.getUserId(), userInfoDto.getRole(), userInfoDto.getType()));
        responseDto.setRefreshToken(JwtTokenUtil.generateRefreshToken(userInfoDto.getUserId(), userInfoDto.getRole(), userInfoDto.getType()));
        responseDto.setTokenType("Bearer");
        responseDto.setUser(userInfoDto);
        return responseDto;
    }

    @Transactional(readOnly = true)
    public List<ImageInfoResponseDto> getImages(String accessToken, Integer warehouseId) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(accessToken));
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        List<ImageInfoResponseDto> images = warehouse.getWarehouseImages()
            .stream().map(ImageInfoResponseDto::new).collect(Collectors.toList());

        if(images.isEmpty()) throw new WarehouseImageNotFoundException();

        return images;
    }
}
