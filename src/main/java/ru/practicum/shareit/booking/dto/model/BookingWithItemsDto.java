package ru.practicum.shareit.booking.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;

import java.time.LocalDateTime;

/**
 * Шаблон для прикрепления к классу ItemDto
 * содержит только id предмета и пользователя
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingWithItemsDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private Enum<BookingStatusEnum> status;
}
