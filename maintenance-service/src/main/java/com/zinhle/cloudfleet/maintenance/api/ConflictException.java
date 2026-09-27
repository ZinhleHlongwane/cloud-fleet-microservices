package com.zinhle.cloudfleet.maintenance.api;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
