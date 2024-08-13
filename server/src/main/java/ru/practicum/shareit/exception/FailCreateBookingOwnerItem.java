package ru.practicum.shareit.exception;

/**
 * Исключение при попытке владельца вещи создать запрос на ее бронирование
 */
public class FailCreateBookingOwnerItem extends RuntimeException {
    public FailCreateBookingOwnerItem(final String message) {
        super(message);
    }
}