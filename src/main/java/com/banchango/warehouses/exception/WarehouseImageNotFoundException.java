package com.banchango.warehouses.exception;

import com.banchango.common.exception.NotFoundException;

public class WarehouseImageNotFoundException extends NotFoundException {

    public static final String MESSAGE = "해당 창고는 등록된 이미지가 존재하지 않습니다.";

    public WarehouseImageNotFoundException(String message) {
        super(message);
    }

    public WarehouseImageNotFoundException() {
        this(MESSAGE);
    }

}