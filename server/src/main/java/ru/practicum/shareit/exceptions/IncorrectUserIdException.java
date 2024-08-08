package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке запросить пользователя с несуществующим id
 */
public class IncorrectUserIdException extends RuntimeException {
    public IncorrectUserIdException(final String message) {
        super(message);
    }
}