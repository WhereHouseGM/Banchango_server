package com.banchango.warehouses.exception;

import com.banchango.common.exception.NotFoundException;

public class WarehouseNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 카테고리로 등록된 창고 결과가 존재하지 않습니다.";

    public WarehouseNotFoundException(String message) {
        super(message);
    }

    public WarehouseNotFoundException() {
        this(MESSAGE);
    }
}
