package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке создать пользователя с уже существующим email
 */
public class FailEmailException extends Exception {
    public FailEmailException(final String message) {
        super(message);
    }
}