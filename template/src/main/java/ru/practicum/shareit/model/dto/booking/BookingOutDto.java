package ru.practicum.shareit.model.dto.booking;

import lombok.*;
import ru.practicum.shareit.model.dto.item.ItemOutDto;
import ru.practicum.shareit.model.enums.BookingStatusEnum;
import ru.practicum.shareit.model.dto.user.UserDto;

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