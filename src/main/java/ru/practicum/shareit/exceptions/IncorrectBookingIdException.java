package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке запросить бронирование с несуществующим id
 */
public class IncorrectBookingIdException extends RuntimeException {
    public IncorrectBookingIdException(final String message) {
        super(message);
    }
}