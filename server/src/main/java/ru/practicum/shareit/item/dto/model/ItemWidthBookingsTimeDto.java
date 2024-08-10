package ru.practicum.shareit.item.dto.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.model.BookingWithItemsDto;
import ru.practicum.shareit.request.dto.model.RequestOutDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Item шаблон для передачи данных
 * с полями для 'последнего' и 'следующего бронирований'
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ItemWidthBookingsTimeDto {
    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private Integer numberOfRentals;
    private Boolean available;
    private RequestOutDto request;
    private BookingWithItemsDto lastBooking;
    private BookingWithItemsDto nextBooking;
    private List<CommentOutDto> comments;
}
