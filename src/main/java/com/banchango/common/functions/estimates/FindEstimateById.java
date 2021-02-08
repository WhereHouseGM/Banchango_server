package com.banchango.common.functions.estimates;

import com.banchango.domain.estimates.Estimates;
import com.banchango.domain.estimates.EstimatesRepository;
import com.banchango.estimates.exception.EstimateNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class FindEstimateById implements Function<Integer, Estimates> {

    private final EstimatesRepository estimatesRepository;

    @Override
    public Estimates apply(Integer estimateId) {
        return estimatesRepository.findById(estimateId).orElseThrow(EstimateNotFoundException::new);

    }
}
