package com.banchango.admin.service;

import com.banchango.admin.dto.*;
import com.banchango.admin.exception.AdminInvalidAccessException;
import com.banchango.admin.exception.WaitingWarehousesNotFoundException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.mainitemtypes.MainItemTypesRepository;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.estimates.dto.EstimateSearchDto;
import com.banchango.estimates.exception.EstimateNoContentException;
import com.banchango.warehouses.dto.WarehouseSummaryDto;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import com.banchango.warehouses.projection.WarehouseIdAndNameAndAddressProjection;
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

    @Value("${banchango.no_image.url}")
    private String noImageUrl;

    private void doubleCheckAdminAccess(Integer userId) {
        Users user = usersRepository.findById(userId).orElseThrow(AdminInvalidAccessException::new);
        if(!user.getRole().equals(UserRole.ADMIN)) throw new AdminInvalidAccessException();
    }

    @Transactional(readOnly = true)
    public WarehouseInsertRequestResponseListDto findWaitingWarehouses(String token, PageRequest pageRequest, WarehouseStatus status) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(token));
        List<Warehouses> warehouses = warehousesRepository.findWarehousesByStatusOrderByCreatedAt(status, pageRequest);
        if(warehouses.size() == 0) throw new WaitingWarehousesNotFoundException();
        return WarehouseInsertRequestResponseListDto.builder()
                .requests(warehouses.stream().map(WarehouseInsertRequestResponseDto::new).collect(Collectors.toList())).build();
    }

    @Transactional(readOnly = true)
    public WarehouseAdminDetailResponseDto getSpecificWarehouseInfo(String token, Integer warehouseId) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(token));
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        return new WarehouseAdminDetailResponseDto(warehouse, noImageUrl);
    }

    @Transactional
    public WarehouseAdminDetailResponseDto updateWarehouse(WarehouseAdminUpdateRequestDto requestDto, String token, Integer warehouseId) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(token));
        Warehouses warehouse = warehousesRepository.findById(warehouseId).orElseThrow(WarehouseIdNotFoundException::new);
        if(!mainItemTypesRepository.findByWarehouseId(warehouseId).equals(requestDto.getMainItemTypes())) {
            mainItemTypesRepository.deleteByWarehouseId(warehouseId);
        }
        warehouse.update(requestDto);
        return new WarehouseAdminDetailResponseDto(warehouse, noImageUrl);
    }

    public List<EstimateSearchDto> getEstimates(String acccessToken, EstimateStatus status, PageRequest pageRequest) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(acccessToken));
        List<Estimates> estimates;
        if (status == null) estimates = estimatesRepository.findByOrderByIdDesc(pageRequest);
        else estimates = estimatesRepository.findByStatusOrderByIdDesc(status, pageRequest);

        if(estimates.isEmpty()) throw new EstimateNoContentException();

        return estimates.stream()
            .map(estimate -> {
                EstimateSearchDto estimateSearchResponseDto = new EstimateSearchDto(estimate);
                Optional<WarehouseIdAndNameAndAddressProjection> optionalProjection = warehousesRepository.findById(estimate.getWarehouseId(), WarehouseIdAndNameAndAddressProjection.class);

                if(optionalProjection.isPresent()) {
                    WarehouseIdAndNameAndAddressProjection projection = optionalProjection.get();
                    WarehouseSummaryDto warehouseSummaryDto = WarehouseSummaryDto.builder()
                        .warehouseId(projection.getId())
                        .name(projection.getName())
                        .address(projection.getAddress())
                        .build();

                    estimateSearchResponseDto.updateWarehouse(warehouseSummaryDto);
                }
                return estimateSearchResponseDto;
            })
            .collect(Collectors.toList());
    }

    public void updateEstimateStatus(String accessToken, Integer estimateId, EstimateStatusUpdateRequestDto estimateStatusUpdateRequestDto) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(accessToken));
        estimatesRepository.updateStatusById(estimateId, estimateStatusUpdateRequestDto.getStatus());
    }
}
