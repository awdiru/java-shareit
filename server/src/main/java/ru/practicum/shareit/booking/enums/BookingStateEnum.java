package ru.practicum.shareit.booking.enums;

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

    public static BookingStateEnum from(String stringState) {
        for (BookingStateEnum state : values())
            if (state.name().equalsIgnoreCase(stringState))
                return state;

        return ALL;
    }
}