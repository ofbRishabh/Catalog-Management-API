package com.product.productmanegement.utils.exceptionHandlers.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}