package ru.practicum.shareit.booking.dto.model;

import lombok.*;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.item.dto.model.item.ItemOutDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * Шаблон исходящих запросов на бронирование
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BookingOutDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemOutDto item;
    private UserDto booker;
    private Enum<BookingStatusEnum> status;
}