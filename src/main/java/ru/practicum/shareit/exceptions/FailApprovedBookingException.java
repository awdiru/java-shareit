package ru.practicum.shareit.exceptions;

/**
 * Исключение при попытке дважды подтвердить статус бронирования
 */
public class FailApprovedBookingException extends Exception {
    public FailApprovedBookingException(final String message) {
        super(message);
    }
}
