package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке запросить данный не владельцем вещи/брониорвания
 */
public class IncorrectOwnerIdException extends Exception {
    public IncorrectOwnerIdException(final String message) {
        super(message);
    }

}
