package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.dto.booking.BookingIncDto;
import ru.practicum.shareit.model.dto.booking.BookingOutDto;
import ru.practicum.shareit.model.dto.booking.BookingWithItemsDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

/**
 * Конвертер Booking классов
 */
@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public Booking toBookingFromBookingIncDto(BookingIncDto booking) {
        return booking == null ? null :
                new Booking(null,
                        booking.getStart(),
                        booking.getEnd(),
                        null,
                        null,
                        null);
    }

    public BookingOutDto toBookingOutDtoFromBooking(Booking booking) {
        return booking == null ? null :
                new BookingOutDto(booking.getId(),
                        booking.getStart(),
                        booking.getEnd(),
                        itemMapper.toItemDtoFromItem(booking.getItem()),
                        userMapper.toUserDto(booking.getBooker()),
                        booking.getStatus());
    }

    public BookingWithItemsDto toBookingWithItemsDtoFromBooking(Booking booking) {
        return booking == null ? null :
                new BookingWithItemsDto(booking.getId(),
                        booking.getStart(),
                        booking.getEnd(),
                        booking.getItem().getId(),
                        booking.getBooker().getId(),
                        booking.getStatus());
    }
}