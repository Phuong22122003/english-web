package com.english.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppException extends RuntimeException{
    private String message;
    private int code;
}
