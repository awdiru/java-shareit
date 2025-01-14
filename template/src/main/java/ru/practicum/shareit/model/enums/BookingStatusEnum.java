package ru.practicum.shareit.model.enums;

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