package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке создать запрос на бронирование с некорректными временными рамками
 */
public class IncorrectBookingTimeException extends RuntimeException {
    public IncorrectBookingTimeException(final String message) {
        super(message);
    }
}