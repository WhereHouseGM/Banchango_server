package com.banchango.warehouses.controller;

import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.exception.BadRequestException;
import com.banchango.common.interceptor.ValidateRequired;
import com.banchango.domain.warehouses.ItemTypeName;
import com.banchango.warehouses.dto.WarehouseDetailResponseDto;
import com.banchango.warehouses.dto.WarehouseInsertRequestDto;
import com.banchango.warehouses.dto.WarehouseSearchDto;
import com.banchango.warehouses.dto.WarehouseSearchResponseDto;
import com.banchango.warehouses.service.WarehousesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
        warehousesService.saveAgencyWarehouse(warehouseInsertRequestDto, accessToken);

        return new BasicMessageResponseDto("창고가 정상적으로 등록 되었습니다");
    }

    @GetMapping("/v3/warehouses")
    public WarehouseSearchResponseDto getAllWarehouses(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) ItemTypeName category,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        List<WarehouseSearchDto> warehouses;
        PageRequest pageRequest = PageRequest.of(page, size);

        if(address != null && category == null) warehouses = warehousesService.getWarehousesByAddress(address, pageRequest);
        else if(address == null && category != null) warehouses = warehousesService.getWarehousesByMainItemType(category, pageRequest);
        else if(address == null && category == null) warehouses = warehousesService.getWarehouses(pageRequest);
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

    /**
     * <p>
     *     TODO : 창고 정보 수정 API
     * </p>
     */
    /*
    @PatchMapping("/v2/warehouses/{warehouseId}")
        public void updateWarehouseInfo(@PathVariable Integer warehouseId, HttpServletResponse response) {
    }
    */

}
