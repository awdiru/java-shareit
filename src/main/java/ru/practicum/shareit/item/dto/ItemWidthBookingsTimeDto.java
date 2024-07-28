package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.model.BookingWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Item шаблон для передачи данных
 * с полями для 'последнего' и 'следующего бронирований'
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemWidthBookingsTimeDto {
    private Long id;
    private String name;
    private String description;
    private User owner;
    private Integer numberOfRentals;
    private Boolean available;
    private ItemRequest request;
    private BookingWithItemsDto lastBooking;
    private BookingWithItemsDto nextBooking;
    private List<CommentOutDto> comments;
}
