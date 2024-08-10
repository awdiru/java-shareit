package ru.practicum.shareit.exception;

/**
 * Исключение при попытке запросить Request с несуществующим id
 */
public class IncorrectRequestIdException extends RuntimeException {
    public IncorrectRequestIdException(final String message) {
        super(message);
    }
}