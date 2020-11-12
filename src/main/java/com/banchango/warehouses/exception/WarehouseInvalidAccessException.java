package com.banchango.warehouses.exception;

public class WarehouseInvalidAccessException extends WarehouseException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 창고에 대한 정보를 수정 또는 삭제할 수 있는 권한이 없습니다.";

    public WarehouseInvalidAccessException() {
        super(MESSAGE);
    }
}
