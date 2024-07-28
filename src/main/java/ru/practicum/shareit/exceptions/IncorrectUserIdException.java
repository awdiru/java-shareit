package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке пользователя с несуществующим id
 */
public class IncorrectUserIdException extends Exception {
    public IncorrectUserIdException(final String message) {
        super(message);
    }
}
