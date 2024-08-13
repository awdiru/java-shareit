package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;
import ru.practicum.shareit.booking.dto.model.BookingWithItemsDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

/**
 * Конвертер Booking классов
 */
@Component
public class BookingMapper {
    private final ItemMapper itemMapper = new ItemMapper();
    private final UserMapper userMapper = new UserMapper();

    public Booking toBookingFromBookingIncDto(BookingIncDto booking) {
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
                itemMapper.toItemDtoFromItem(booking.getItem()),
                userMapper.toUserDto(booking.getBooker()),
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