package ru.practicum.shareit.exception;

/**
 * Исключение при попытке дважды подтвердить статус бронирования
 */
public class FailApprovedBookingException extends RuntimeException {
    public FailApprovedBookingException(final String message) {
        super(message);
    }
}