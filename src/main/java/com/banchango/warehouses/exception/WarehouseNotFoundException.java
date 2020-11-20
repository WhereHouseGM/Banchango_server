package com.banchango.warehouses.exception;

public class WarehouseNotFoundException extends WarehouseException{
    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 카테고리로 등록된 창고 결과가 존재하지 않습니다.";

    public WarehouseNotFoundException() {
        super(MESSAGE);
    }
}
