package ru.practicum.shareit.booking.enums;

import java.util.Optional;

/**
 * Статус запросов вещей пользователя
 */
public enum BookingStateEnum {
    /**
     * Все
     */
    ALL,
    /**
     * Текущие
     */
    CURRENT,
    /**
     * Завершенные
     */
    PAST,
    /**
     * Будущие
     */
    FUTURE,
    /**
     * Ожидающие подтверждения
     */
    WAITING,
    /**
     * Отклоненные
     */
    REJECTED;

    public static Optional<BookingStateEnum> from(String stringState) {
        for (BookingStateEnum state : values())
            if (state.name().equalsIgnoreCase(stringState))
                return Optional.of(state);

        return Optional.empty();
    }
}