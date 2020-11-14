package com.banchango.warehouses.exception;

public class WarehouseAlreadyRegisteredException extends WarehouseException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 사용자는 이미 창고를 등록했으므로 더 이상 등록할 수 없습니다.";

    public WarehouseAlreadyRegisteredException() {
        super(MESSAGE);
    }
}
