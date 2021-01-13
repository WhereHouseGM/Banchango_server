package com.banchango.estimates.exception;

import com.banchango.common.exception.NotFoundException;

public class EstimateNotFoundException extends NotFoundException {
    private static final String MESSAGE = "견적 문의를 찾을 수 없습니다";

    public EstimateNotFoundException() {
        super(MESSAGE);
    }
}
