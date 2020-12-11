package com.banchango.warehouses.exception;

public class WarehouseMainImageNotFoundException extends WarehouseException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 창고는 등록된 메인 이미지가 존재하지 않습니다.";

    public WarehouseMainImageNotFoundException() {
        super(MESSAGE);
    }
}
