package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.booking.BookingWithItemsDto;
import ru.practicum.shareit.comment.CommentOutDto;
import ru.practicum.shareit.request.RequestOutDto;
import ru.practicum.shareit.user.UserDto;

import java.util.List;

/**
 * Item DTO шаблон для передачи данных
 * с полями для 'последнего' и 'следующего' бронирований
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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