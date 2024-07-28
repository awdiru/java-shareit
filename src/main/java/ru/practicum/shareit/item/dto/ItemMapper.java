package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Конвертер Item классов
 */
@Component
public class ItemMapper {
    private final BookingRepository reposBooking;
    private final CommentRepository reposComment;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemMapper(BookingRepository reposBooking, BookingMapper bookingMapper, CommentRepository reposComment, CommentMapper commentMapper) {
        this.reposBooking = reposBooking;
        this.reposComment = reposComment;
        this.bookingMapper = bookingMapper;

        this.commentMapper = commentMapper;
    }

    public ItemDto toItemDtoFromItem(final Item item) {
        if (item == null) {
            return null;
        }
        List<CommentOutDto> comments = reposComment.findAllByItem(item.getId())
                .stream()
                .map(commentMapper::toCommentOutDtoFromComment)
                .collect(Collectors.toList());

        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner(),
                item.getNumberOfRentals(),
                item.getAvailable(),
                item.getRequest(),
                comments);
    }

    public Item toItemFromItemDto(final ItemDto itemDto) {

        if (itemDto == null) {
            return null;
        }

        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getOwner(),
                itemDto.getNumberOfRentals(),
                itemDto.getAvailable(),
                itemDto.getRequest());
    }

    public ItemWidthBookingsTimeDto toItemWidthBookingsTimeDtoFromItem(Item item) {
        List<CommentOutDto> comments = reposComment.findAllByItem(item.getId())
                .stream()
                .map(commentMapper::toCommentOutDtoFromComment)
                .collect(Collectors.toList());


        return new ItemWidthBookingsTimeDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner(),
                item.getNumberOfRentals(),
                item.getAvailable(),
                item.getRequest(),
                bookingMapper.toBookingWithItemsDtoFromBooking(
                        reposBooking.findLastBooking(item.getId(), LocalDateTime.now())),
                bookingMapper.toBookingWithItemsDtoFromBooking(
                        reposBooking.findNextBooking(item.getId(), LocalDateTime.now())),
                comments);
    }
}