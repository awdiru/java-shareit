package ru.practicum.shareit.exception;

/**
 * Исключение при попытке вещь с несуществующим id
 */
public class IncorrectItemIdException extends RuntimeException {
    public IncorrectItemIdException(final String message) {
        super(message);
    }

}