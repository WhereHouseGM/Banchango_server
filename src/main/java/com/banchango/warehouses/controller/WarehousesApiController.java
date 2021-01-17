package com.banchango.warehouses.controller;

import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.exception.BadRequestException;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.domain.mainitemtypes.MainItemType;
import com.banchango.warehouses.dto.*;
import com.banchango.warehouses.service.WarehousesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class WarehousesApiController {

    private final WarehousesService warehousesService;

    @ValidateRequired
    @PostMapping("/v3/warehouses")
    public BasicMessageResponseDto registerAgency(
            @Valid @RequestBody WarehouseInsertRequestDto warehouseInsertRequestDto,
            @RequestAttribute(name = "accessToken") String accessToken
    ) {
        return warehousesService.saveAgencyWarehouse(warehouseInsertRequestDto, accessToken);
    }

    @GetMapping("/v3/warehouses")
    public WarehouseSearchResponseDto getAllWarehouses(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) List<MainItemType> mainItemTypes,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        List<WarehouseSearchDto> warehouses;
        PageRequest pageRequest = PageRequest.of(page, size);

        if(address != null && mainItemTypes == null) warehouses = warehousesService.getWarehousesByAddress(address, pageRequest);
        else if(address == null && mainItemTypes != null) warehouses = warehousesService.getWarehousesByMainItemTypes(mainItemTypes, pageRequest);
        else if(address == null && mainItemTypes == null) warehouses = warehousesService.getWarehouses(pageRequest);
        else throw new BadRequestException("mainitem 또는 address 하나만 있어야합니다.");

        return new WarehouseSearchResponseDto(warehouses);
    }

    @ValidateRequired
    @DeleteMapping("/v3/warehouses/{warehouseId}")
    public BasicMessageResponseDto delete(
            @PathVariable Integer warehouseId,
            @RequestAttribute(name = "accessToken") String accessToken
    ) {
        warehousesService.delete(warehouseId, accessToken);

        return new BasicMessageResponseDto("창고가 정상적으로 삭제되었습니다.");
    }

    @GetMapping("/v3/warehouses/{warehouseId}")
    public WarehouseDetailResponseDto getWarehouseById(
        @PathVariable Integer warehouseId
    ) {
        return warehousesService.getSpecificWarehouseInfo(warehouseId);
    }

    @ValidateRequired
    @PutMapping("/v3/warehouses/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseDetailResponseDto updateWarehouseInfo(
        @PathVariable Integer warehouseId,
        @Valid @RequestBody WarehouseUpdateRequestDto warehouseUpdateRequestDto,
        @RequestAttribute(name = "accessToken") String accessToken
    ) {
        return warehousesService.updateWarehouse(accessToken, warehouseId, warehouseUpdateRequestDto);
    }
}
