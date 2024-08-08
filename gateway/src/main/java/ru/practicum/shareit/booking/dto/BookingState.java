package ru.practicum.shareit.booking.dto;

import java.util.Optional;

/**
 * Модификатор запросов
 */
public enum BookingState {
    /**
     * Все
     */
    ALL,
    /**
     * Текущие
     */
    CURRENT,
    /**
     * Будущие
     */
    FUTURE,
    /**
     * Завершенные
     */
    PAST,
    /**
     * Отклоненные
     */
    REJECTED,
    /**
     * Ожидающие подтверждения
     */
    WAITING;

    /**
     * Преобразование из строки в статус
     *
     * @param stringState строка
     * @return статус
     */
    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}