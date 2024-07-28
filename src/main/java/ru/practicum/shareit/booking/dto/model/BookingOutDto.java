package ru.practicum.shareit.booking.dto.model;

import lombok.*;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Шаблон исходящих запросов на бронирование
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookingOutDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Enum<BookingStatusEnum> status;
}
