package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;
import ru.practicum.shareit.booking.dto.model.BookingWithItemsDto;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

/**
 * Конвертер Booking классов
 */
@Component
public class BookingMapper {
    private final ItemRepository reposItem;
    private final UserRepository reposUser;

    @Autowired
    public BookingMapper(ItemRepository reposItem, UserRepository reposUser) {
        this.reposItem = reposItem;
        this.reposUser = reposUser;
    }

    public Booking toBookingFromBookingIncDto(BookingIncDto bookingIncomingDto, Long userId) {
        return new Booking(null,
                bookingIncomingDto.getStart(),
                bookingIncomingDto.getEnd(),
                reposItem.findById((long) bookingIncomingDto.getItemId()),
                reposUser.findById((long) userId),
                BookingStatusEnum.WAITING);
    }

    public BookingOutDto toBookingOutDtoFromBooking(Booking booking) {
        if (booking == null)
            return null;
        return new BookingOutDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus());
    }

    public BookingWithItemsDto toBookingWithItemsDtoFromBooking(Booking booking) {
        if (booking == null)
            return null;
        return new BookingWithItemsDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus());
    }
}
