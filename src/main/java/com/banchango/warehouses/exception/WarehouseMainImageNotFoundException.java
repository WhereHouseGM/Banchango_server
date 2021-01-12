package com.banchango.warehouses.exception;

import com.banchango.common.exception.NotFoundException;

public class WarehouseMainImageNotFoundException extends NotFoundException {

    public static final String MESSAGE = "해당 창고는 등록된 메인 이미지가 존재하지 않습니다.";

    public WarehouseMainImageNotFoundException(String message) {
        super(message);
    }

    public WarehouseMainImageNotFoundException() {
        this(MESSAGE);
    }

}
