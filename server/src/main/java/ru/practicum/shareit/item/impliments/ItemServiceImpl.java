package ru.practicum.shareit.item.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.IncorrectCommentatorException;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.exception.IncorrectRequestIdException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.model.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация сервиса для ItemController
 */
@Service
@Slf4j
class ItemServiceImpl implements ItemService {
    private final RequestRepository reposRequest;
    private final ItemRepository reposItem;
    private final UserRepository reposUser;
    private final CommentRepository reposComment;
    private final BookingRepository reposBooking;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Autowired
    public ItemServiceImpl(final RequestRepository reposRequest,
                           final ItemRepository reposItem,
                           final UserRepository reposUser,
                           final CommentRepository reposComment,
                           final BookingRepository reposBooking,
                           final ItemMapper itemMapper,
                           final CommentMapper commentMapper,
                           final BookingMapper bookingMapper) {
        this.reposRequest = reposRequest;
        this.reposItem = reposItem;
        this.reposUser = reposUser;
        this.reposComment = reposComment;
        this.reposBooking = reposBooking;
        this.itemMapper = itemMapper;
        this.commentMapper = commentMapper;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public ItemOutDto createItem(final ItemIncDto itemDto,
                                 final Long userId) {

        Item item = itemMapper.toItemFromItemIncDto(itemDto);

        item.setOwner(reposUser.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден.")));

        if (itemDto.getRequestId() != null)
            item.setRequest(reposRequest.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new IncorrectRequestIdException("Запрос с id " + itemDto.getRequestId() + " не найден")));

        return itemMapper.toItemDtoFromItem(reposItem.save(item));
    }

    @Override
    public ItemOutDto updateItem(final Long itemId,
                                 final ItemIncDto itemDto,
                                 final Long userId) {

        Item item = reposItem.findById(itemId)
                .orElseThrow(() -> new IncorrectItemIdException("Вещь с id " + itemId + " не найдена."));

        if (!item.getOwner().getId().equals(userId))
            throw new IncorrectUserIdException("Редактировать вещь может только ее владелец.");

        if (itemDto.getName() != null) item.setName(itemDto.getName());

        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());

        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

        ItemOutDto itemOutDto = itemMapper.toItemDtoFromItem(reposItem.save(item));
        itemOutDto.setComments(
                reposComment.findAllByItem(itemId)
                        .stream()
                        .map(commentMapper::toCommentOutDtoFromComment)
                        .toList()
        );
        return itemOutDto;
    }

    @Override
    public ItemWidthBookingsTimeDto getItem(final Long itemId,
                                            final Long userId) {

        Item item = reposItem.findById(itemId)
                .orElseThrow(() -> new IncorrectItemIdException("Вещь с id " + itemId + " не найдена"));

        ItemWidthBookingsTimeDto itemWidthBookingsTimeDto = itemMapper.toItemWidthBookingsTimeDtoFromItem(item);

        itemWidthBookingsTimeDto.setComments(
                reposComment.findAllByItem(itemId)
                        .stream()
                        .map(commentMapper::toCommentOutDtoFromComment)
                        .toList()
        );

        if (!item.getOwner().getId().equals(userId))
            return itemWidthBookingsTimeDto;

        Pageable paging = PageRequest.of(0, 1);

        List<Booking> lastBookings = reposBooking.findLastBooking(item.getId(), LocalDateTime.now(), paging).toList();
        if (!lastBookings.isEmpty())
            itemWidthBookingsTimeDto.setLastBooking(bookingMapper
                    .toBookingWithItemsDtoFromBooking(lastBookings.getFirst()));

        List<Booking> nextBookings = reposBooking.findNextBooking(item.getId(), LocalDateTime.now(), paging).toList();
        if (!nextBookings.isEmpty())
            itemWidthBookingsTimeDto.setNextBooking(bookingMapper
                    .toBookingWithItemsDtoFromBooking(nextBookings.getFirst()));

        return itemWidthBookingsTimeDto;
    }

    @Override
    public List<ItemWidthBookingsTimeDto> getItemsUser(final Long userId,
                                                       final Integer from,
                                                       final Integer size) {
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
                .toList();
    }

    @Override
    public List<ItemOutDto> searchItems(final String text) {

        if (text == null || text.isBlank())
            return new ArrayList<>();

        return reposItem.searchByNameOrDescription("%" + text.toLowerCase() + "%")
                .stream()
                .map(itemMapper::toItemDtoFromItem)
                .peek(itemDto ->
                        itemDto.setComments(
                                reposComment.findAllByItem(itemDto.getId())
                                        .stream()
                                        .map(commentMapper::toCommentOutDtoFromComment)
                                        .toList()
                        ))
                .toList();
    }

    @Override
    public CommentOutDto addComment(final CommentIncDto commentIncDto,
                                    final Long itemId,
                                    final Long userId) {

        List<Booking> bookings = reposBooking
                .searchForBookerIdAndItemId(userId, itemId, LocalDateTime.now(), PageRequest.of(0, 1))
                .toList();

        if (bookings.isEmpty())
            throw new IncorrectCommentatorException(
                    "Комментарии могут оставлять только те пользователи, которые брали вещь в аренду");

        Comment comment = commentMapper.toCommentFromCommentIncDto(commentIncDto);

        comment.setAuthor(reposUser.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден")));

        comment.setItem(reposItem.findById(itemId)
                .orElseThrow(() -> new IncorrectItemIdException("Вещь с id " + itemId + " не найдена.")));

        comment.setCreated(LocalDateTime.now());
        reposComment.save(comment);
        return commentMapper.toCommentOutDtoFromComment(comment);
    }
}