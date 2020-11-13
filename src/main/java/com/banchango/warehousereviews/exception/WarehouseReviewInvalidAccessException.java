package com.banchango.warehousereviews.exception;

public class WarehouseReviewInvalidAccessException extends WarehouseReviewException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 리뷰를 삭제하기 위한 권한이 없습니다.";

    public WarehouseReviewInvalidAccessException() {
        super(MESSAGE);
    }
}
