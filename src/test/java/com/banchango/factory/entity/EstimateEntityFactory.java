package com.banchango.factory.entity;

import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
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
    private final EstimatesRepository estimatesRepository;

    @Autowired
    EstimateEntityFactory(EstimatesRepository estimatesRepository, EstimateItemEntityFactory estimateItemEntityFactory) {
        this.estimatesRepository = estimatesRepository;
        this.estimateItemEntityFactory = estimateItemEntityFactory;
    }

    @Transactional
    public Estimates createInProgressWithEstimateItems(Integer warehouseId, Integer userId) {
        Estimates estimate = create(warehouseId, userId, EstimateStatus.IN_PROGRESS);

        List<EstimateItems> estimateItems = IntStream.range(0, 3)
            .mapToObj(number -> estimateItemEntityFactory.create(estimate))
            .collect(Collectors.toList());

        estimate.setEstimateItems(estimateItems);

        return estimate;
    }

    public Estimates createInProgressWithoutEstimateItems(Integer warehouseId, Integer userId) {
        return create(warehouseId, userId, EstimateStatus.IN_PROGRESS);
    }

    @Transactional
    public Estimates createReceptedWithEstimateItems(Integer warehouseId, Integer userId) {
        Estimates estimate = create(warehouseId, userId, EstimateStatus.RECEPTED);

        List<EstimateItems> estimateItems = IntStream.range(0, 3)
            .mapToObj(number -> estimateItemEntityFactory.create(estimate))
            .collect(Collectors.toList());

        estimate.setEstimateItems(estimateItems);

        return estimate;
    }

    public Estimates createReceptedWithoutEstimateItems(Integer warehouseId, Integer userId) {
        return create(warehouseId, userId, EstimateStatus.RECEPTED);
    }

    @Transactional
    public Estimates createDoneWithEstimateItems(Integer warehouseId, Integer userId) {
        Estimates estimate = create(warehouseId, userId, EstimateStatus.DONE);

        List<EstimateItems> estimateItems = IntStream.range(0, 3)
            .mapToObj(number -> estimateItemEntityFactory.create(estimate))
            .collect(Collectors.toList());

        estimate.setEstimateItems(estimateItems);

        return estimate;
    }

    public Estimates createDoneWithoutEstimateItems(Integer warehouseId, Integer userId) {
        return create(warehouseId, userId, EstimateStatus.DONE);
    }

    private Estimates create(Integer warehouseId, Integer userId, EstimateStatus status) {
        Estimates estimate = Estimates.builder()
            .warehouseId(warehouseId)
            .userId(userId)
            .content(CONTENT)
            .monthlyAverageRelease(MONTHLY_AVERAGE_RELEASE)
            .status(status)
            .estimateItems(new ArrayList<>())
            .build();

        return estimatesRepository.save(estimate);
    }
}
