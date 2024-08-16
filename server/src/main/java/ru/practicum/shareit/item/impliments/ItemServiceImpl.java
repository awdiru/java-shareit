package ru.practicum.shareit.item.impliments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.model.dto.comment.CommentIncDto;
import ru.practicum.shareit.model.dto.comment.CommentOutDto;
import ru.practicum.shareit.exception.IncorrectCommentatorException;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.exception.IncorrectRequestIdException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.dto.item.ItemIncDto;
import ru.practicum.shareit.model.dto.item.ItemOutDto;
import ru.practicum.shareit.model.dto.item.ItemWidthBookingsTimeDto;
import ru.practicum.shareit.model.dto.item.ItemWithoutCommentsDto;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.constants.TopicNames.ADD_COMMENT;
import static ru.practicum.shareit.constants.TopicNames.CREATING_ITEM_ON_REQUESTS;

/**
 * Реализация сервиса для ItemController
 */
@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    private final KafkaTemplate<String, ItemWithoutCommentsDto> itemWithoutCommentsDtoKafka;
    private final KafkaTemplate<String, CommentOutDto> commentOutDtoKafka;

    @Override
    public ItemOutDto createItem(final ItemIncDto itemDto,
                                 final Long userId) {

        Item item = itemMapper.toItemFromItemIncDto(itemDto);

        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден.")));

        if (itemDto.getRequestId() != null) {
            item.setRequest(requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new IncorrectRequestIdException("Запрос с id " + itemDto.getRequestId() + " не найден")));

            itemWithoutCommentsDtoKafka.send(CREATING_ITEM_ON_REQUESTS, itemMapper.toItemWithoutCommentsDtoFromItem(item));
        }
        return itemMapper.toItemDtoFromItem(itemRepository.save(item));
    }

    @Override
    public ItemOutDto updateItem(final Long itemId,
                                 final ItemIncDto itemDto,
                                 final Long userId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IncorrectItemIdException("Вещь с id " + itemId + " не найдена."));

        if (!item.getOwner().getId().equals(userId))
            throw new IncorrectUserIdException("Редактировать вещь может только ее владелец.");

        if (itemDto.getName() != null) item.setName(itemDto.getName());

        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());

        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

        ItemOutDto itemOutDto = itemMapper.toItemDtoFromItem(itemRepository.save(item));
        itemOutDto.setComments(
                commentRepository.findAllByItem(itemId)
                        .stream()
                        .map(commentMapper::toCommentOutDtoFromComment)
                        .toList()
        );
        return itemOutDto;
    }

    @Override
    public ItemWidthBookingsTimeDto getItem(final Long itemId,
                                            final Long userId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IncorrectItemIdException("Вещь с id " + itemId + " не найдена"));

        ItemWidthBookingsTimeDto itemWidthBookingsTimeDto = itemMapper.toItemWidthBookingsTimeDtoFromItem(item);

        itemWidthBookingsTimeDto.setComments(
                commentRepository.findAllByItem(itemId)
                        .stream()
                        .map(commentMapper::toCommentOutDtoFromComment)
                        .toList()
        );

        if (!item.getOwner().getId().equals(userId))
            return itemWidthBookingsTimeDto;

        Pageable paging = PageRequest.of(0, 1);

        List<Booking> lastBookings = bookingRepository.findLastBooking(item.getId(), LocalDateTime.now(), paging).toList();
        if (!lastBookings.isEmpty())
            itemWidthBookingsTimeDto.setLastBooking(bookingMapper
                    .toBookingWithItemsDtoFromBooking(lastBookings.getFirst()));

        List<Booking> nextBookings = bookingRepository.findNextBooking(item.getId(), LocalDateTime.now(), paging).toList();
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
        return itemRepository.findAllByOwnerId(userId, paging)
                .stream()
                .map(itemMapper::toItemWidthBookingsTimeDtoFromItem)
                .peek(itemWidthBookingsTimeDto ->
                        itemWidthBookingsTimeDto.setComments(
                                commentRepository.findAllByItem(itemWidthBookingsTimeDto.getId())
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

        return itemRepository.searchByNameOrDescription("%" + text.toLowerCase() + "%")
                .stream()
                .map(itemMapper::toItemDtoFromItem)
                .peek(itemDto ->
                        itemDto.setComments(
                                commentRepository.findAllByItem(itemDto.getId())
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

        List<Booking> bookings = bookingRepository
                .searchForBookerIdAndItemId(userId, itemId, LocalDateTime.now(), PageRequest.of(0, 1))
                .toList();

        if (bookings.isEmpty())
            throw new IncorrectCommentatorException(
                    "Комментарии могут оставлять только те пользователи, которые брали вещь в аренду");

        Comment comment = commentMapper.toCommentFromCommentIncDto(commentIncDto);

        comment.setAuthor(userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден")));

        comment.setItem(itemRepository.findById(itemId)
                .orElseThrow(() -> new IncorrectItemIdException("Вещь с id " + itemId + " не найдена.")));

        comment.setCreated(LocalDateTime.now());

        CommentOutDto commentOutDto = commentMapper.toCommentOutDtoFromComment(commentRepository.save(comment));
        commentOutDtoKafka.send(ADD_COMMENT, commentOutDto);
        return commentOutDto;
    }
}