package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.enums.BookingStatusEnum;
import ru.practicum.shareit.item.ItemOutDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

/**
 * Booking DTO шаблон для исходящих ответов
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