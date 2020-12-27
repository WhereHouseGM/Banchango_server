package com.banchango.warehouses.exception;

import com.banchango.common.exception.NoContentException;

public class WarehouseSearchException extends NoContentException {
    public static final String MESSAGE = "해당 키워드로 검색된 결과가 없습니다.";

    public WarehouseSearchException() {
        super(MESSAGE);
    }
}
