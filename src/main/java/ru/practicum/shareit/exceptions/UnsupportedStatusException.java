package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке передать некорректный модификатор запроса
 */
public class UnsupportedStatusException extends RuntimeException {
    public UnsupportedStatusException(String message) {
        super(message);
    }
}
