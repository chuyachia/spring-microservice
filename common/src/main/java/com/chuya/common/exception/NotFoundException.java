package com.chuya.common.exception;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s);
    }
}
