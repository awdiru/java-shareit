package ru.practicum.shareit.model.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Booking DTO шаблон для входящих запросов
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(description = "Сущность бронирования")
public class BookingIncDto {
    @Schema(description = "id пользователя")
    private Long itemId;
    @FutureOrPresent
    @Schema(description = "Начало бронирования", defaultValue = "2025-08-15T00:00:00.000Z")
    private LocalDateTime start;
    @Future
    @Schema(description = "Окончание бронирования", defaultValue = "2025-08-20T00:00:00.000Z")
    private LocalDateTime end;
}