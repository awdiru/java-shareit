package ru.practicum.shareit.exception;

/**
 * Исключение при попытке запросить бронирование с несуществующим id
 */
public class IncorrectBookingIdException extends RuntimeException {
    public IncorrectBookingIdException(final String message) {
        super(message);
    }
}