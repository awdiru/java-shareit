package ru.practicum.shareit.booking.dto.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Шаблон входящих запросов на бронирование
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookingIncDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}