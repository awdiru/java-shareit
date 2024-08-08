package ru.practicum.shareit.booking.enums;

/**
 * Статус бронирования
 */
public enum BookingStatusEnum {
    /**
     * Ожидает подтверждения.
     */
    WAITING,
    /**
     * Подтверждено владельцем.
     */
    APPROVED,
    /**
     * Отменено владельцем.
     */
    REJECTED,
    /**
     * Отменено пользователем.
     */
    CANCELED
}