package com.banchango.factory.entity;

import com.banchango.domain.estimateitems.EstimateItem;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimate;
import com.banchango.domain.estimates.EstimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Component
public class EstimateEntityFactory {
    public static final String CONTENT = "CONTENT";
    public static final Integer MONTHLY_AVERAGE_RELEASE = 1299;

    private final EstimateItemEntityFactory estimateItemEntityFactory;
    private final EstimateRepository estimateRepository;

    @Autowired
    EstimateEntityFactory(EstimateRepository estimateRepository, EstimateItemEntityFactory estimateItemEntityFactory) {
        this.estimateRepository = estimateRepository;
        this.estimateItemEntityFactory = estimateItemEntityFactory;
    }

    @Transactional
    public Estimate createInProgressWithEstimateItems(Integer warehouseId, Integer userId) {
        Estimate estimate = create(warehouseId, userId, EstimateStatus.IN_PROGRESS);

        List<EstimateItem> estimateItems = IntStream.range(0, 3)
            .mapToObj(number -> estimateItemEntityFactory.create(estimate))
            .collect(Collectors.toList());

        estimate.setEstimateItems(estimateItems);

        return estimate;
    }

    public Estimate createInProgressWithoutEstimateItems(Integer warehouseId, Integer userId) {
        return create(warehouseId, userId, EstimateStatus.IN_PROGRESS);
    }

    @Transactional
    public Estimate createReceptedWithEstimateItems(Integer warehouseId, Integer userId) {
        Estimate estimate = create(warehouseId, userId, EstimateStatus.RECEPTED);

        List<EstimateItem> estimateItems = IntStream.range(0, 3)
            .mapToObj(number -> estimateItemEntityFactory.create(estimate))
            .collect(Collectors.toList());

        estimate.setEstimateItems(estimateItems);

        return estimate;
    }

    public Estimate createReceptedWithoutEstimateItems(Integer warehouseId, Integer userId) {
        return create(warehouseId, userId, EstimateStatus.RECEPTED);
    }

    @Transactional
    public Estimate createDoneWithEstimateItems(Integer warehouseId, Integer userId) {
        Estimate estimate = create(warehouseId, userId, EstimateStatus.DONE);

        List<EstimateItem> estimateItems = IntStream.range(0, 3)
            .mapToObj(number -> estimateItemEntityFactory.create(estimate))
            .collect(Collectors.toList());

        estimate.setEstimateItems(estimateItems);

        return estimate;
    }

    public Estimate createDoneWithoutEstimateItems(Integer warehouseId, Integer userId) {
        return create(warehouseId, userId, EstimateStatus.DONE);
    }

    private Estimate create(Integer warehouseId, Integer userId, EstimateStatus status) {
        Estimate estimate = Estimate.builder()
            .warehouseId(warehouseId)
            .userId(userId)
            .content(CONTENT)
            .monthlyAverageRelease(MONTHLY_AVERAGE_RELEASE)
            .status(status)
            .estimateItems(new ArrayList<>())
            .build();

        return estimateRepository.save(estimate);
    }
}
