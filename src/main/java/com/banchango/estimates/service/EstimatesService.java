package com.banchango.estimates.service;

import com.banchango.auth.token.JwtTokenUtil;
import com.banchango.common.dto.BasicMessageResponseDto;
import com.banchango.common.functions.users.FindUserById;
import com.banchango.common.functions.warehouses.FindWarehouseById;
import com.banchango.common.service.EmailSender;
import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehouses.WarehouseIdAndNameAndAddressProjection;
import com.banchango.domain.warehouses.WarehouseStatus;
import com.banchango.domain.warehouses.Warehouses;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.estimates.dto.EstimateInsertRequestDto;
import com.banchango.estimates.dto.EstimateSearchDto;
import com.banchango.estimates.exception.EstimateNotFoundException;
import com.banchango.tools.EmailContent;
import com.banchango.users.exception.ForbiddenUserIdException;
import com.banchango.users.exception.UserNotFoundException;
import com.banchango.warehouses.dto.WarehouseSummaryDto;
import com.banchango.warehouses.exception.WarehouseIsNotViewableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EstimatesService {
    private final EstimatesRepository estimatesRepository;
    private final WarehousesRepository warehousesRepository;
    private final EmailSender emailSender;
    private final UsersRepository usersRepository;
    private final FindWarehouseById findWarehouseById;
    private final FindUserById findUserById;

    @Transactional
    public BasicMessageResponseDto saveEstimate(String accessToken, EstimateInsertRequestDto estimateInsertRequestDto) {
        Integer userId = JwtTokenUtil.extractUserId(accessToken);
        Users user = findUserById.apply(userId);

        Warehouses warehouse = findWarehouseById.apply(estimateInsertRequestDto.getWarehouseId());
        if(!warehouse.getStatus().equals(WarehouseStatus.VIEWABLE)) throw new WarehouseIsNotViewableException();

        Estimates newEstimate = Estimates.builder()
            .content(estimateInsertRequestDto.getContent())
            .userId(userId)
            .warehouseId(estimateInsertRequestDto.getWarehouseId())
            .status(EstimateStatus.RECEPTED)
            .monthlyAverageRelease(estimateInsertRequestDto.getMonthlyAverageRelease())
            .build();

        estimatesRepository.save(newEstimate);

        List<EstimateItems> newEstimateItems = estimateInsertRequestDto.getEstimateItems()
            .stream()
            .map(estimateItemDto -> estimateItemDto.toEntity(newEstimate))
            .collect(Collectors.toList());

        newEstimate.setEstimateItems(newEstimateItems);

        EmailContent emailContent = new EmailContent("[반창고]견적 문의 등록 안내", "안녕하세요, 반창고 입니다!", user.getName() + " 회원님의 " + warehouse.getName() + " 창고에 대한 견적 문의가 정상적으로 등록되었습니다. 추후 영업 팀의 검토 후 연락드리겠습니다.", "문의사항은 이 이메일로 답변주세요.", "반창고 허브", "https://banchangohub.com");
        return emailSender.send(user.getEmail(), emailContent, true);
    }

    @Transactional(readOnly = true)
    public List<EstimateSearchDto> getEstimatesByUserId(String accessToken, Integer userId) {
        Integer userIdFromAccessToken = JwtTokenUtil.extractUserId(accessToken);

        if(!userIdFromAccessToken.equals(userId)) throw new ForbiddenUserIdException();

        List<EstimateSearchDto> estimates = estimatesRepository.findByUserId(userId)
            .stream()
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
            .filter(searchDto -> searchDto.getWarehouse() != null)
            //.filter(searchDto -> searchDto.getWarehouse().getWarehouseId() != null && searchDto.getWarehouse().getName() != null && searchDto.getWarehouse().getAddress() != null)
            .collect(Collectors.toList());
        if(estimates.size() == 0) throw new EstimateNotFoundException();

        return estimates;
    }
}
