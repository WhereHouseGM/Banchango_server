package com.banchango.warehouses.exception;

public class WarehouseSearchException extends WarehouseException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 키워드로 검색된 결과가 없습니다.";

    public WarehouseSearchException() {
        super(MESSAGE);
    }

}
