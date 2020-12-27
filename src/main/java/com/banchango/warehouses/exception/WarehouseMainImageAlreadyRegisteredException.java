package com.banchango.warehouses.exception;

import com.banchango.common.exception.NotAcceptableException;

public class WarehouseMainImageAlreadyRegisteredException extends NotAcceptableException {
    public static final String MESSAGE = "이미 창고의 메인 사진이 등록되어 있습니다.";

    public WarehouseMainImageAlreadyRegisteredException(String message) {
        super(message);
    }

    public WarehouseMainImageAlreadyRegisteredException() {
        this(MESSAGE);
    }
}
