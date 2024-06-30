package ru.practicum.shareit.booking;

public enum BookingStatus {
    /**
     * Бронирование ожидает подтверждения.
     */
    WAITING,
    /**
     * Бронирование подтверждено владельцем.
     */
    APPROVED,
    /**
     * Бронирование отменено владельцем.
     */
    REJECTED,
    /**
     * Бронирование отменено пользователем.
     */
    CANCELED
}
