package com.banchango.warehouses.exception;

public class WarehouseAttachmentLimitException extends WarehouseException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "등록할 수 있는 사진의 최대 개수(5개)를 초과했습니다.";

    public WarehouseAttachmentLimitException() {
        super(MESSAGE);
    }
}
