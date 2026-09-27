package com.zinhle.cloudfleet.telemetry.api;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
