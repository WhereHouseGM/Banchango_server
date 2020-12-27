package com.banchango.warehouses.exception;

import com.banchango.common.exception.NotAcceptableException;

public class WarehouseExtraImageLimitException extends NotAcceptableException{
    public static final String MESSAGE = "등록할 수 있는 사진의 최대 개수(5개)를 초과했습니다.";

    public WarehouseExtraImageLimitException(String message) {
        super(message);
    }

    public WarehouseExtraImageLimitException() {
        this(MESSAGE);
    }
}
