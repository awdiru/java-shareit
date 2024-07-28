package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке создать запрос на бронирование с некорректными временными рамками
 */
public class IncorrectBookingTimeException extends Exception {
    public IncorrectBookingTimeException(final String message) {
        super(message);
    }
}
