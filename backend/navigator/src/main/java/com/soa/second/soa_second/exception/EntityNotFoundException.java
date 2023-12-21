package com.soa.second.soa_second.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
        super("Сущность не найдена");
    }
}
