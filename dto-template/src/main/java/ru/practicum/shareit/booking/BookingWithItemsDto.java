package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.enums.BookingStatusEnum;

import java.time.LocalDateTime;

/**
 * Booking DTO шаблон для прикрепления к классу ItemDto
 * содержит только id предмета и пользователя
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BookingWithItemsDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private Enum<BookingStatusEnum> status;
}