package ru.practicum.shareit.exception;

/**
 * Исключение при попытке создать пользователя с пустым email
 */
public class IncorrectEmailException extends RuntimeException {
    public IncorrectEmailException(final String message) {
        super(message);
    }
}
