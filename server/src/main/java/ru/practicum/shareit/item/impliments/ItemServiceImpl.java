package ru.practicum.shareit.item.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.IncorrectCommentatorException;
import ru.practicum.shareit.exceptions.IncorrectItemIdException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для ItemController
 */
@Service
@Slf4j
class ItemServiceImpl implements ItemService {
    private final ItemRepository reposItem;
    private final UserRepository reposUser;
    private final CommentRepository reposComment;
    private final BookingRepository reposBooking;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Autowired
    public ItemServiceImpl(final ItemRepository reposItem,
                           final UserRepository reposUser,
                           final CommentRepository reposComment,
                           final BookingRepository reposBooking,
                           final ItemMapper itemMapper,
                           final CommentMapper commentMapper,
                           final BookingMapper bookingMapper) {
        this.reposItem = reposItem;
        this.reposUser = reposUser;
        this.reposComment = reposComment;
        this.reposBooking = reposBooking;
        this.itemMapper = itemMapper;
        this.commentMapper = commentMapper;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public ItemDto createItem(final ItemDto itemDto, final Long userId) {
        itemDto.setOwner(reposUser.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден.")));

        Item item = itemMapper.toItemFromItemDto(itemDto);
        return itemMapper.toItemDtoFromItem(reposItem.save(item));
    }

    @Override
    public ItemDto updateItem(final Long itemId, ItemDto itemDto, final Long userId) {

        Item item = reposItem.findById(itemId)
                .orElseThrow(() -> new IncorrectItemIdException("Вещь с id " + itemId + " не найдена."));

        if (!item.getOwner().getId().equals(userId))
            throw new IncorrectUserIdException("Редактировать вещь может только ее владелец.");

        if (itemDto.getName() != null) item.setName(itemDto.getName());

        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());

        if (itemDto.getNumberOfRentals() != null) item.setNumberOfRentals(itemDto.getNumberOfRentals());

        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

        ItemDto itemOutDto = itemMapper.toItemDtoFromItem(reposItem.save(item));
        itemOutDto.setComments(
                reposComment.findAllByItem(itemId)
                        .stream()
                        .map(commentMapper::toCommentOutDtoFromComment)
                        .toList()
        );
        return itemOutDto;
    }

    @Override
    public ItemWidthBookingsTimeDto getItem(final Long itemId, Long userId) {
        Item item = reposItem.findById(itemId)
                .orElseThrow(()->new IncorrectItemIdException("Вещь с id " + itemId + " не найдена"));

        ItemWidthBookingsTimeDto itemWidthBookingsTimeDto = itemMapper.toItemWidthBookingsTimeDtoFromItem(item);

        itemWidthBookingsTimeDto.setComments(
                reposComment.findAllByItem(itemId)
                        .stream()
                        .map(commentMapper::toCommentOutDtoFromComment)
                        .toList()
        );

        if (!item.getOwner().getId().equals(userId))
            return itemWidthBookingsTimeDto;

        itemWidthBookingsTimeDto.setLastBooking(
                bookingMapper.toBookingWithItemsDtoFromBooking(
                        reposBooking.findLastBooking(item.getId(), LocalDateTime.now())));

        itemWidthBookingsTimeDto.setNextBooking(
                bookingMapper.toBookingWithItemsDtoFromBooking(
                        reposBooking.findNextBooking(item.getId(), LocalDateTime.now())));


        return itemWidthBookingsTimeDto;
    }

    @Override
    public List<ItemWidthBookingsTimeDto> getItemsUser(final Long userId, Integer from, Integer size) {
        Pageable paging = PageRequest.of(from, size);
        return reposItem.findAllByOwnerId(userId, paging)
                .stream()
                .map(itemMapper::toItemWidthBookingsTimeDtoFromItem)
                .peek(itemWidthBookingsTimeDto ->
                        itemWidthBookingsTimeDto.setComments(
                                reposComment.findAllByItem(itemWidthBookingsTimeDto.getId())
                                        .stream()
                                        .map(commentMapper::toCommentOutDtoFromComment)
                                        .toList()
                        ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank())
            return new ArrayList<>();

        return reposItem.searchByNameOrDescription("%" + text.toLowerCase() + "%")
                .stream()
                .map(itemMapper::toItemDtoFromItem)
                .peek(itemDto -> itemDto.setComments(
                        reposComment.findAllByItem(itemDto.getId())
                                .stream()
                                .map(commentMapper::toCommentOutDtoFromComment)
                                .toList()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public CommentOutDto addComment(CommentIncDto commentIncDto, Long itemId, Long userId) {

        Booking booking = reposBooking.searchForBookerIdAndItemId(userId, itemId, LocalDateTime.now());

        if (booking == null || !booking.getStatus().equals(BookingStatusEnum.APPROVED))
            throw new IncorrectCommentatorException(
                    "Комментарии могут оставлять только те пользователи, которые брали вещь в аренду");

        log.info("ItemServiceImpl: addComment");

        Comment comment = commentMapper.toCommentFromCommentIncDto(commentIncDto);

        comment.setAuthor(reposUser.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Редактировать вещь может только ее владелец.")));

        comment.setItem(reposItem.findById(itemId)
                .orElseThrow(() -> new IncorrectItemIdException("Вещь с id " + itemId + " не найдена.")));

        comment.setCreated(LocalDateTime.now());
        reposComment.save(comment);
        return commentMapper.toCommentOutDtoFromComment(comment);
    }
}
