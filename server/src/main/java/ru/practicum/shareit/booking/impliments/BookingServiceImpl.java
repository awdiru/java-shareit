package ru.practicum.shareit.booking.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;
import ru.practicum.shareit.booking.enums.BookingStateEnum;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * Реализация сервиса для BookingController
 */
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository reposBooking;
    private final UserRepository reposUser;
    private final ItemRepository reposItem;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(final BookingRepository reposBooking,
                              final ItemRepository reposItem,
                              final UserRepository repsUser,
                              final BookingMapper bookingMapper) {
        this.reposBooking = reposBooking;
        this.reposUser = repsUser;
        this.reposItem = reposItem;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingOutDto createBooking(BookingIncDto bookingIncDto, Long userId) {

        Booking booking = bookingMapper.toBookingFromBookingIncDto(bookingIncDto);
        booking.setBooker(reposUser.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден")));

        booking.setItem(reposItem.findById(bookingIncDto.getItemId())
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
        booking = reposBooking.save(booking);
        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public BookingOutDto approvedBooking(Long userId, Long bookingId, Boolean approved) {

        Booking booking = reposBooking.findById(bookingId)
                .orElseThrow(() -> new IncorrectBookingIdException("Бронирование с id " + bookingId + " не найдено"));

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
            Item item = booking.getItem();

            if (item.getNumberOfRentals() != null) {
                item.setNumberOfRentals(item.getNumberOfRentals() + 1);
            } else item.setNumberOfRentals(1);

            reposItem.save(item);

        } else if (booking.getBooker().getId().equals(userId))
            booking.setStatus(BookingStatusEnum.CANCELED);

        else booking.setStatus(BookingStatusEnum.REJECTED);

        reposBooking.save(booking);
        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public BookingOutDto getBooking(Long userId, Long bookingId) {

        Booking booking = reposBooking.findById(bookingId)
                .orElseThrow(() -> new IncorrectBookingIdException("Бронирование с id " + bookingId + " не найдено"));

        if (!(booking.getItem().getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)))
            throw new IncorrectUserIdException("Получение данных о конкретном бронировании " +
                    "может быть выполнено либо автором бронирования, либо владельцем вещи");

        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public List<BookingOutDto> getAllBookingsUser(Long userId, String state, Integer from, Integer size) {
        Pageable paging = PageRequest.of(from, size);

        BookingStateEnum stateEnum = BookingStateEnum.from(state).get();
        reposUser.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден"));

        Stream<Booking> stream = switch (stateEnum) {
            case CURRENT -> reposBooking
                    .findAllByBookerForStatusCurrent(userId, LocalDateTime.now(), paging).stream();
            case PAST -> reposBooking
                    .findAllByBookerForStatusPast(userId, LocalDateTime.now(), paging).stream();
            case FUTURE -> reposBooking
                    .findAllByBookerForStatusFuture(userId, LocalDateTime.now(), paging).stream();
            case WAITING -> reposBooking
                    .findAllByBookerForStatusWaitingOrRejected(userId, BookingStatusEnum.WAITING, paging).stream();
            case REJECTED -> reposBooking
                    .findAllByBookerForStatusWaitingOrRejected(userId, BookingStatusEnum.REJECTED, paging).stream();
            default -> reposBooking
                    .findAllByBooker(userId, paging).stream();
        };
        return stream
                .map(bookingMapper::toBookingOutDtoFromBooking)
                .toList();
    }

    @Override
    public List<BookingOutDto> getAllBookingsItemsUser(Long userId, String state, Integer from, Integer size) {
        Pageable paging = PageRequest.of(from, size);

        BookingStateEnum stateEnum = BookingStateEnum.from(state).get();
        reposUser.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден"));

        Stream<Booking> stream = switch (stateEnum) {
            case CURRENT -> reposBooking
                    .findAllBookingsItemsUserForStatusCurrent(userId, LocalDateTime.now(), paging).stream();
            case PAST -> reposBooking
                    .findAllBookingsItemsUserForStatusPast(userId, LocalDateTime.now(), paging).stream();
            case FUTURE -> reposBooking
                    .findAllBookingsItemsUserForStatusFuture(userId, LocalDateTime.now(), paging).stream();
            case WAITING -> reposBooking
                    .findAllBookingsItemsUserForStatusWaitingOrRejected(userId, BookingStatusEnum.WAITING, paging).stream();
            case REJECTED -> reposBooking
                    .findAllBookingsItemsUserForStatusWaitingOrRejected(userId, BookingStatusEnum.REJECTED, paging).stream();
            default -> reposBooking
                    .findAllBookingsItemsUser(userId, paging).stream();
        };
        return stream
                .map(bookingMapper::toBookingOutDtoFromBooking)
                .toList();
    }
}