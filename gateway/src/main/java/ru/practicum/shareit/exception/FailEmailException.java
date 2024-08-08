package ru.practicum.shareit.exception;

/**
 * Исключение при попытке создать пользователя с уже существующим email
 */
public class FailEmailException extends RuntimeException {
    public FailEmailException(final String message) {
        super(message);
    }
}
