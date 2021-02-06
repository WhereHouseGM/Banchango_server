package com.banchango.warehouses.exception;

import com.banchango.common.exception.ForbiddenException;

public class WarehouseImageNotUpdatableException extends ForbiddenException {
    public static final String MESSAGE = "[삭제] 또는 [반려] 상태인 창고의 사진은 변경할 수 없습니다.";

    public WarehouseImageNotUpdatableException(String message) {
        super(message);
    }

    public WarehouseImageNotUpdatableException() {
        this(MESSAGE);
    }
}
