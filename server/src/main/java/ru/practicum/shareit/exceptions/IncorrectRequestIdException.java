package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке запросить Request с несуществующим id
 */
public class IncorrectRequestIdException extends RuntimeException {
    public IncorrectRequestIdException(final String message) {
        super(message);
    }
}