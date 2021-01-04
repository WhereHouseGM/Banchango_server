package com.banchango.admin.service;

import com.banchango.admin.dto.WarehouseInsertRequestResponseDto;
import com.banchango.admin.exception.AdminInvalidAccessException;
import com.banchango.admin.exception.WaitingWarehousesNotFoundException;
import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final WarehousesRepository warehousesRepository;
    private final UsersRepository usersRepository;

    public List<WarehouseInsertRequestResponseDto> findWaitingWarehouses(String token, PageRequest pageRequest) {
        Integer userId = JwtTokenUtil.extractUserId(token);
        Users user = usersRepository.findById(userId).orElseThrow(AdminInvalidAccessException::new);
        if(!user.getRole().equals(UserRole.ADMIN)) {
            throw new AdminInvalidAccessException();
        }
        List<Warehouses> warehouses = warehousesRepository.findWarehousesByStatusOrderByCreatedAt(WarehouseStatus.IN_PROGRESS, pageRequest);
        if(warehouses.size() == 0) throw new WaitingWarehousesNotFoundException();
        return warehouses.stream().map(WarehouseInsertRequestResponseDto::new).collect(Collectors.toList());
    }
}
