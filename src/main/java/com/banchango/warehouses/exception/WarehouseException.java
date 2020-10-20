package com.banchango.warehouses.exception;

public abstract class WarehouseException extends Exception{

    private static final long serialVersionUID = 1L;
    public WarehouseException(String message) {
        super(message);
    }
}
