package com.banchango.images.exception;

public class FileRemoveException extends Exception{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "파일 삭제에 실패했습니다.";

    public FileRemoveException(){
        super(MESSAGE);
    }
}
