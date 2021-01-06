package com.banchango.admin.service;

import com.banchango.admin.dto.WarehouseAdminDetailResponseDto;
import com.banchango.admin.dto.WarehouseAdminUpdateRequestDto;
import com.banchango.admin.dto.WarehouseInsertRequestResponseDto;
import com.banchango.admin.dto.WarehouseInsertRequestResponseListDto;
import com.banchango.admin.exception.AdminInvalidAccessException;
import com.banchango.admin.exception.WaitingWarehousesNotFoundException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.mainitemtypes.MainItemTypesRepository;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final WarehousesRepository warehousesRepository;
    private final UsersRepository usersRepository;
    private final MainItemTypesRepository mainItemTypesRepository;

    @Value("${banchango.no_image.url}")
    private String noImageUrl;

    private void doubleCheckAdminAccess(Integer userId) {
        Users user = usersRepository.findById(userId).orElseThrow(AdminInvalidAccessException::new);
        if(!user.getRole().equals(UserRole.ADMIN)) throw new AdminInvalidAccessException();
    }

    @Transactional(readOnly = true)
    public WarehouseInsertRequestResponseListDto findWaitingWarehouses(String token, PageRequest pageRequest) {
        doubleCheckAdminAccess(JwtTokenUtil.extractUserId(token));
        List<Warehouses> warehouses = warehousesRepository.findWarehousesByStatusOrderByCreatedAt(WarehouseStatus.IN_PROGRESS, pageRequest);
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
}
