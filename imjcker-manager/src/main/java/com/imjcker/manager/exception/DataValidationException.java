package com.imjcker.manager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataValidationException extends Exception{
    private String message;
}
