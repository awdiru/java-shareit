package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private long item;
    private long booker;
    private Enum<BookingStatus> status;
}
