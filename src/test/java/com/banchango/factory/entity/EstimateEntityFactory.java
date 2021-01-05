package com.banchango.factory.entity;

import com.banchango.domain.estimateitems.EstimateItems;
import com.banchango.domain.estimates.EstimateStatus;
import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Component
public class EstimateEntityFactory {
    private static final String CONTENT = "CONTENT";

    private EstimateItemEntityFactory estimateItemEntityFactory;
    private EstimatesRepository estimatesRepository;

    @Autowired
    EstimateEntityFactory(EstimatesRepository estimatesRepository, EstimateItemEntityFactory estimateItemEntityFactory) {
        this.estimatesRepository = estimatesRepository;
        this.estimateItemEntityFactory = estimateItemEntityFactory;
    }

    @Transactional
    public Estimates createInProgressWithEstimateItems(Integer warehouseId, Integer userId) {
        Estimates estimate = create(warehouseId, userId, EstimateStatus.IN_PROGRESS);

        EstimateItems item1 = estimateItemEntityFactory.create(estimate);
        EstimateItems item2 = estimateItemEntityFactory.create(estimate);
        EstimateItems item3 = estimateItemEntityFactory.create(estimate);

        estimate.getEstimateItems().add(item1);
        estimate.getEstimateItems().add(item2);
        estimate.getEstimateItems().add(item3);

        return estimate;
    }

    public Estimates createInProgressWithoutEstimateItems(Integer warehouseId, Integer userId) {
        return create(warehouseId, userId, EstimateStatus.IN_PROGRESS);
    }

    private Estimates create(Integer warehouseId, Integer userId, EstimateStatus status) {
        Estimates estimate = Estimates.builder()
            .warehouseId(warehouseId)
            .userId(userId)
            .content(CONTENT)
            .status(status)
            .estimateItems(new ArrayList<>())
            .build();

        return estimatesRepository.save(estimate);
    }
}
