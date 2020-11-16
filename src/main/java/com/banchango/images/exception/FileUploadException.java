package com.banchango.images.exception;

public class FileUploadException extends Exception {
    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "파일 업로드에 실패했습니다.";

    public FileUploadException() {
        super(MESSAGE);
    }
}
