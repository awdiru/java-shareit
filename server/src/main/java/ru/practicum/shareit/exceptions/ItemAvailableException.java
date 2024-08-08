package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке сделать запрос на бронирование для вещи, закрытой для бронирования
 */
public class ItemAvailableException extends RuntimeException {
    public ItemAvailableException(final String message) {
        super(message);
    }
}