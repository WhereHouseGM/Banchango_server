package com.banchango.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorResponseDto {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
