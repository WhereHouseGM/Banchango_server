package com.banchango.warehousereviews.exception;

public class WarehouseReviewNotFoundException extends WarehouseReviewException {

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 창고에 등록된 리뷰가 없습니다.";

    public WarehouseReviewNotFoundException() {
        super(MESSAGE);
    }
}
