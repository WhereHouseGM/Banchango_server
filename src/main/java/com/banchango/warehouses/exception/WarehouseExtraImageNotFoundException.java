package com.banchango.warehouses.exception;

import com.banchango.common.exception.NoContentException;

public class WarehouseExtraImageNotFoundException extends NoContentException {

    public static final String MESSAGE = "해당 사진이름으로 저장된 정보가 없으므로 삭제에 실패했습니다.";

    public WarehouseExtraImageNotFoundException(String message) {
        super(message);
    }

    public WarehouseExtraImageNotFoundException() {
        this(MESSAGE);
    }
}
