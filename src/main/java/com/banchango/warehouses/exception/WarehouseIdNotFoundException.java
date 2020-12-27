package com.banchango.warehouses.exception;

import com.banchango.common.exception.NoContentException;

public class WarehouseIdNotFoundException extends NoContentException {
    public static final String MESSAGE = "해당 id로 조회된 창고 결과가 없습니다.";

    public WarehouseIdNotFoundException() {
        super(MESSAGE);
    }
}
