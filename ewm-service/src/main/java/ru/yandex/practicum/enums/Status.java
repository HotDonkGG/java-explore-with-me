package ru.yandex.practicum.enums;

import ru.yandex.practicum.exception.ValidationException;

public enum Status {

    PENDING,
    CONFIRMED,
    CANCELED,
    REJECTED;

    public static Status getStatusValue(String status) {
        try {
            return Status.valueOf(status);
        } catch (Exception e) {
            throw new ValidationException("Unknown status: " + status);
        }
    }
}