package com.banchango.warehouses.exception;

public class WarehouseAttachmentNotFoundException extends WarehouseException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 사진이름으로 저장된 정보가 없으므로 삭제에 실패했습니다.";

    public WarehouseAttachmentNotFoundException() {
        super(MESSAGE);
    }
}
