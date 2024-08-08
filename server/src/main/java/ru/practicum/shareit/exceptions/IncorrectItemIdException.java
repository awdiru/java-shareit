package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке вещь с несуществующим id
 */
public class IncorrectItemIdException extends RuntimeException {
    public IncorrectItemIdException(final String message) {
        super(message);
    }

}
