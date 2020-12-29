package com.banchango.warehouses.exception;

import com.banchango.common.exception.ForbiddenException;

public class WarehouseNotViewableException extends ForbiddenException {
    public static final String MESSAGE = "아직 해당 창고를 조회할 수 없습니다";

    public WarehouseNotViewableException() {
        super(MESSAGE);
    }
}
