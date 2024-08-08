package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;
import ru.practicum.shareit.booking.dto.model.BookingWithItemsDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;

/**
 * Конвертер Booking классов
 */
@Component
public class BookingMapper {
    public Booking toBookingFromBookingIncDto(BookingIncDto booking, Long userId) {
        if (booking == null) return null;
        return new Booking(null,
                booking.getStart(),
                booking.getEnd(),
                null,
                null,
                BookingStatusEnum.WAITING);
    }

    public BookingOutDto toBookingOutDtoFromBooking(Booking booking) {
        if (booking == null) return null;
        return new BookingOutDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus());
    }

    public BookingWithItemsDto toBookingWithItemsDtoFromBooking(Booking booking) {
        if (booking == null) return null;
        return new BookingWithItemsDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus());
    }
}