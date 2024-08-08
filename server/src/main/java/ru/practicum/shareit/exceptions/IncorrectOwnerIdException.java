package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке запросить данный не владельцем вещи/бронирования
 */
public class IncorrectOwnerIdException extends RuntimeException {
    public IncorrectOwnerIdException(final String message) {
        super(message);
    }

}