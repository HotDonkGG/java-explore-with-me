package ru.yandex.practicum.exceptions;

public class StatisticsValidationException extends RuntimeException {
    public StatisticsValidationException(String message) {
        super(message);
    }
}