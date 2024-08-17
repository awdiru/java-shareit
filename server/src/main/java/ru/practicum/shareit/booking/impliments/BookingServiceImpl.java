package ru.practicum.shareit.booking.impliments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.dto.booking.BookingIncDto;
import ru.practicum.shareit.model.dto.booking.BookingOutDto;
import ru.practicum.shareit.model.enums.BookingStateEnum;
import ru.practicum.shareit.model.enums.BookingStatusEnum;
import ru.practicum.shareit.sender.DataSender;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static ru.practicum.shareit.constants.TopicNames.BOOKING_STATUS_TOPIC;

/**
 * Реализация сервиса для BookingController
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;
    private final DataSender sender;

    @Override
    public BookingOutDto createBooking(final BookingIncDto bookingIncDto,
                                       final Long userId) {

        Booking booking = bookingMapper.toBookingFromBookingIncDto(bookingIncDto);
        booking.setBooker(userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден")));

        booking.setItem(itemRepository.findById(bookingIncDto.getItemId())
                .orElseThrow(() -> new IncorrectItemIdException("Предмет с id " + bookingIncDto.getItemId() + " не найден")));

        if (!booking.getItem().getAvailable())
            throw new ItemAvailableException(
                    "Предмет с id " + booking.getItem().getId() + " не доступен для бронирования");

        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().equals(booking.getStart()))
            throw new IncorrectBookingTimeException(
                    "Время окончания бронирования не может быть раньше или равно времени начала бронирования");

        if (booking.getItem().getOwner().getId().equals(userId))
            throw new FailCreateBookingOwnerItem("Владелец вещи не может ее забронировать");

        booking.setStatus(BookingStatusEnum.WAITING);
        booking = bookingRepository.save(booking);
        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public BookingOutDto approvedBooking(final Long userId,
                                         final Long bookingId,
                                         final Boolean approved) {

        BookingStatusEnum status;

        User owner = userRepository.findById(userId).get();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IncorrectBookingIdException("Бронирование с id " + bookingId + " не найдено"));

        User booker = userRepository.findById(booking.getBooker().getId()).get();

        Item item = booking.getItem();

        if (booking.getBooker().getId().equals(userId) && approved)
            throw new IncorrectUserIdException("Бронирование может подтвердить только владелец вещи");

        if (!booking.getItem().getOwner().getId().equals(userId))
            throw new IncorrectOwnerIdException("Бронирование может подтвердить только владелец вещи");

        if (booking.getStatus().equals(BookingStatusEnum.APPROVED) || booking.getStatus().equals(BookingStatusEnum.REJECTED))
            throw new FailApprovedBookingException("Подтверждение бронирования уже произошло. Статус бронирования: " + booking.getStatus());

        if (booking.getStatus().equals(BookingStatusEnum.CANCELED))
            throw new FailApprovedBookingException("Бронирование отменено пользователем");

        if (approved) {
            booking.setStatus(BookingStatusEnum.APPROVED);
            status = BookingStatusEnum.APPROVED;
            if (item.getNumberOfRentals() != null) {
                item.setNumberOfRentals(item.getNumberOfRentals() + 1);
            } else item.setNumberOfRentals(1);
            itemRepository.save(item);
        } else if (booking.getBooker().getId().equals(userId)) {
            booking.setStatus(BookingStatusEnum.CANCELED);
            status = BookingStatusEnum.CANCELED;
        } else {
            booking.setStatus(BookingStatusEnum.REJECTED);
            status = BookingStatusEnum.REJECTED;
        }

        sender.sendBookingStatusNotification(
                BOOKING_STATUS_TOPIC,
                owner.getEmail(),
                status,
                item.getName(),
                booker.getEmail()
        );

        bookingRepository.save(booking);
        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public BookingOutDto getBooking(final Long userId,
                                    final Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IncorrectBookingIdException("Бронирование с id " + bookingId + " не найдено"));

        if (!(booking.getItem().getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)))
            throw new IncorrectUserIdException("Получение данных о конкретном бронировании " +
                    "может быть выполнено либо автором бронирования, либо владельцем вещи");

        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public List<BookingOutDto> getAllBookingsUser(final Long userId,
                                                  final String state,
                                                  final Integer from,
                                                  final Integer size) {

        Pageable paging = PageRequest.of(from, size);

        BookingStateEnum stateEnum = BookingStateEnum.from(state).get();
        userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден"));

        Stream<Booking> stream = switch (stateEnum) {
            case CURRENT -> bookingRepository
                    .findAllByBookerForStatusCurrent(userId, LocalDateTime.now(), paging).stream();
            case PAST -> bookingRepository
                    .findAllByBookerForStatusPast(userId, LocalDateTime.now(), paging).stream();
            case FUTURE -> bookingRepository
                    .findAllByBookerForStatusFuture(userId, LocalDateTime.now(), paging).stream();
            case WAITING -> bookingRepository
                    .findAllByBookerForStatusWaitingOrRejected(userId, BookingStatusEnum.WAITING, paging).stream();
            case REJECTED -> bookingRepository
                    .findAllByBookerForStatusWaitingOrRejected(userId, BookingStatusEnum.REJECTED, paging).stream();
            default -> bookingRepository
                    .findAllByBooker(userId, paging).stream();
        };
        return stream
                .map(bookingMapper::toBookingOutDtoFromBooking)
                .toList();
    }

    @Override
    public List<BookingOutDto> getAllBookingsItemsUser(final Long userId,
                                                       final String state,
                                                       final Integer from,
                                                       final Integer size) {

        Pageable paging = PageRequest.of(from, size);

        BookingStateEnum stateEnum = BookingStateEnum.from(state).get();
        userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден"));

        Stream<Booking> stream = switch (stateEnum) {
            case CURRENT -> bookingRepository
                    .findAllBookingsItemsUserForStatusCurrent(userId, LocalDateTime.now(), paging).stream();
            case PAST -> bookingRepository
                    .findAllBookingsItemsUserForStatusPast(userId, LocalDateTime.now(), paging).stream();
            case FUTURE -> bookingRepository
                    .findAllBookingsItemsUserForStatusFuture(userId, LocalDateTime.now(), paging).stream();
            case WAITING -> bookingRepository
                    .findAllBookingsItemsUserForStatusWaitingOrRejected(userId, BookingStatusEnum.WAITING, paging).stream();
            case REJECTED -> bookingRepository
                    .findAllBookingsItemsUserForStatusWaitingOrRejected(userId, BookingStatusEnum.REJECTED, paging).stream();
            default -> bookingRepository
                    .findAllBookingsItemsUser(userId, paging).stream();
        };
        return stream
                .map(bookingMapper::toBookingOutDtoFromBooking)
                .toList();
    }
}