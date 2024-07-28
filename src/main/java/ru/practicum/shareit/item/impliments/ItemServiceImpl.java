package ru.practicum.shareit.item.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
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

    @Autowired
    public ItemServiceImpl(final ItemRepository reposItem,
                           UserRepository reposUser,
                           CommentRepository reposComment,
                           BookingRepository reposBooking,
                           ItemMapper itemMapper,
                           CommentMapper commentMapper) {
        this.reposItem = reposItem;
        this.reposUser = reposUser;
        this.reposComment = reposComment;
        this.reposBooking = reposBooking;
        this.itemMapper = itemMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public ItemDto createItem(final ItemDto itemDto, final long userId)
            throws IncorrectUserIdException {

        if (reposUser.findById(userId) == null) {
            log.warn("ItemServiceImpl: createItem FALSE, Incorrect user id");
            throw new IncorrectUserIdException("Пользователь с id " + userId + " не найден.");
        }
        User user = reposUser.findById(userId);
        itemDto.setOwner(user);
        Item item = itemMapper.toItemFromItemDto(itemDto);
        return itemMapper.toItemDtoFromItem(reposItem.save(item));
    }

    @Override
    public ItemDto updateItem(final long itemId, ItemDto itemDto, final long userId)
            throws IncorrectUserIdException, IncorrectItemIdException {

        Item item = reposItem.findById(itemId);
        if (item == null) {
            log.warn("ItemServiceImpl: updateItem FALSE, Incorrect item id");
            throw new IncorrectItemIdException("Вещь с id " + itemId + " не найдена.");

        } else if (item.getOwner().getId() != userId) {
            log.warn("ItemServiceImpl: updateItem FALSE. Incorrect user ID");
            throw new IncorrectUserIdException("редактировать вещь может только ее владелец.");
        }

        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getNumberOfRentals() != null)
            item.setNumberOfRentals(itemDto.getNumberOfRentals());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        item.setOwner(item.getOwner());
        return itemMapper.toItemDtoFromItem(reposItem.save(item));
    }

    @Override
    public ItemWidthBookingsTimeDto getItem(final long itemId, Long userId) throws IncorrectItemIdException {
        log.info("ItemServiceImpl: getItem");
        Item item = reposItem.findById(itemId);
        if (item == null)
            throw new IncorrectItemIdException("Вещь с id " + itemId + " не найдена");

        ItemWidthBookingsTimeDto itemWidthBookingsTimeDto = itemMapper.toItemWidthBookingsTimeDtoFromItem(item);

        if (item.getOwner().getId().equals(userId))
            return itemWidthBookingsTimeDto;

        itemWidthBookingsTimeDto.setLastBooking(null);
        itemWidthBookingsTimeDto.setNextBooking(null);
        return itemWidthBookingsTimeDto;

    }

    @Override
    public List<ItemWidthBookingsTimeDto> getItemsUser(final long userId) {
        log.info("ItemServiceImpl: getItemsUser");
        return reposItem.findAllByOwnerId(userId).stream()
                .map(itemMapper::toItemWidthBookingsTimeDtoFromItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(final String text) {
        log.info("ItemServiceImpl: searchItems");
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return reposItem.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, text).stream()
                .map(itemMapper::toItemDtoFromItem)
                .collect(Collectors.toList());
    }

    @Override
    public CommentOutDto addComment(CommentIncDto commentIncDto, Long itemId, Long userId) throws IncorrectCommentatorException {
        Booking booking = reposBooking.searchForBookerIdAndItemId(userId, itemId, LocalDateTime.now());
        if (booking == null || !booking.getStatus().equals(BookingStatusEnum.APPROVED))
            throw new IncorrectCommentatorException(
                    "Комментарии могут оставлять только те пользователи, которые брали вещь в аренду");

        Comment comment = commentMapper.toCommentFromCommentIncDto(commentIncDto);
        comment.setAuthor(reposUser.findById((long) userId));
        comment.setItem(reposItem.findById((long) itemId));
        comment.setCreated(LocalDateTime.now());
        reposComment.save(comment);
        return commentMapper.toCommentOutDtoFromComment(comment);
    }
}
