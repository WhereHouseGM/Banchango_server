package com.banchango.admin.exception;

import com.banchango.common.exception.NotFoundException;

public class WaitingWarehousesNotFoundException extends NotFoundException {
    public WaitingWarehousesNotFoundException() {
        super("승인 대기중인 창고가 존재하지 않습니다.");
    }
}
