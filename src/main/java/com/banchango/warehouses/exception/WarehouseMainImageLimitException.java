package com.banchango.warehouses.exception;

public class WarehouseMainImageLimitException extends WarehouseException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 창고는 이미 메인 사진이 있습니다. 기존 사진을 삭제 후 다시 등록해주세요.";

    public WarehouseMainImageLimitException() {
        super(MESSAGE);
    }
}
