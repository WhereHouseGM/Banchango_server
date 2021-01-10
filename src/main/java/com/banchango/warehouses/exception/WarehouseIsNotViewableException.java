package com.banchango.warehouses.exception;

import com.banchango.common.exception.ForbiddenException;

public class WarehouseIsNotViewableException extends ForbiddenException {
    public static final String MESSAGE = "해당 창고를 조회할 수 있는 권한이 없습니다";

    public WarehouseIsNotViewableException(String message) {
        super(message);
    }

    public WarehouseIsNotViewableException() {
        this(MESSAGE);
    }
}
