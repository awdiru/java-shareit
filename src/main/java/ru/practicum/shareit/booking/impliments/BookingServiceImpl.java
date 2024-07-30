package ru.practicum.shareit.booking.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;
import ru.practicum.shareit.booking.enums.BookingStateEnum;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
    public BookingOutDto createBooking(BookingIncDto bookingIncDto, long userId) {

        Booking booking = bookingMapper.toBookingFromBookingIncDto(bookingIncDto, userId);

        if (reposUser.findById(userId) == null) {
            log.warn("BookingServiceImpl : createBooking FAIL, throw IncorrectUserIdException");
            throw new IncorrectUserIdException("Некорректный id пользователя");
        }
        if (booking.getItem() == null) {
            log.warn("BookingServiceImpl : createBooking FAIL, throw IncorrectItemIdException");
            throw new IncorrectItemIdException(
                    "Предмет с id " + bookingIncDto.getItemId() + " не найден");
        }
        if (!booking.getItem().getAvailable()) {
            log.warn("BookingServiceImpl : createBooking FAIL, throw ItemAvailableException");
            throw new ItemAvailableException(
                    "Предмет с id " + booking.getItem().getId() + " не доступен для бронирования");
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().equals(booking.getStart())) {
            log.warn("BookingServiceImpl : createBooking FAIL, throw IncorrectBookingTimeException");
            throw new IncorrectBookingTimeException("Время окончания бронирования не может быть раньше или равно " +
                    "времени начала бронирования");
        }
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            log.warn("BookingServiceImpl : createBooking FAIL, throw IncorrectBookingTimeException");
            throw new IncorrectBookingTimeException("Время окончания бронирования не может быть в прошлом");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            log.warn("BookingServiceImpl : createBooking FAIL, throw IncorrectBookingTimeException");
            throw new IncorrectBookingTimeException("Время начала бронирования не может быть в прошлом");
        }
        if (booking.getItem().getOwner().getId().equals(userId)) {
            log.warn("BookingServiceImpl : createBooking FAIL, throw IncorrectBookingTimeException");
            throw new FailCreateBookingOwnerItem("Владелец вещи не может ее забронировать");
        }
        log.info("BookingServiceImpl : createBooking");
        booking.setBooker(reposUser.findById(userId));
        booking.setStatus(BookingStatusEnum.WAITING);
        booking = reposBooking.save(booking);
        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public BookingOutDto approvedBooking(long userId, long bookingId, Boolean approved) {

        Booking booking = reposBooking.findById(bookingId);
        if (booking == null) {
            log.warn("BookingServiceImpl : approvedBooking FAIL, throw IncorrectBookingIdException");
            throw new IncorrectBookingIdException("Бронирование с id " + bookingId + " не найдено");
        }
        if (userId == booking.getBooker().getId() && approved) {
            log.warn("BookingServiceImpl : approvedBooking FAIL, throw IncorrectUserIdException");
            throw new IncorrectUserIdException("Бронирование может подтвердить только владелец вещи");
        }
        if (userId != booking.getItem().getOwner().getId()) {
            log.warn("BookingServiceImpl : approvedBooking FAIL, throw IncorrectOwnerIdException");
            throw new IncorrectOwnerIdException("Бронирование может подтвердить только владелец вещи");
        }
        if (booking.getStatus().equals(BookingStatusEnum.APPROVED) || booking.getStatus().equals(BookingStatusEnum.REJECTED)) {
            log.warn("BookingServiceImpl : approvedBooking FAIL, throw FailApprovedBookingException");
            throw new FailApprovedBookingException("Подтверждение бронирования уже произошло. Статус бронирования: " + booking.getStatus());
        }
        if (booking.getStatus().equals(BookingStatusEnum.CANCELED)) {
            log.warn("BookingServiceImpl : approvedBooking FAIL, throw FailApprovedBookingException");
            throw new FailApprovedBookingException("Бронирование отменено пользователем");
        }
        if (approved) {
            log.info("BookingServiceImpl : approvedBooking, owner, APPROVED");
            booking.setStatus(BookingStatusEnum.APPROVED);
            Item item = booking.getItem();

            if (item.getNumberOfRentals() != null) {
                item.setNumberOfRentals(item.getNumberOfRentals() + 1);
            } else item.setNumberOfRentals(1);

            reposItem.save(item);

        } else if (userId == booking.getBooker().getId()) {
            log.info("BookingServiceImpl : approvedBooking, booker, CANCELED");
            booking.setStatus(BookingStatusEnum.CANCELED);
        } else {
            log.info("BookingServiceImpl : approvedBooking, owner, REJECTED");
            booking.setStatus(BookingStatusEnum.REJECTED);
        }
        reposBooking.save(booking);
        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public BookingOutDto getBooking(long userId, long bookingId) {

        Booking booking = reposBooking.findById(bookingId);
        if (booking == null) {
            log.warn("BookingServiceImpl : getBooking FAIL, throw IncorrectBookingIdException");
            throw new IncorrectBookingIdException("Бронирование с id " + bookingId + " не найдено");
        }
        if (!(booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId)) {
            log.warn("BookingServiceImpl : getBooking FAIL, throw IncorrectBookingIdException");
            throw new IncorrectUserIdException("Получение данных о конкретном бронировании " +
                    "может быть выполнено либо автором бронирования, либо владельцем вещи");
        }
        log.info("BookingServiceImpl : approvedBooking");
        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public List<BookingOutDto> getAllBookingsUser(long userId, String state) {

        BookingStateEnum stateEnum;
        try {
            stateEnum = Enum.valueOf(BookingStateEnum.class, state);
        } catch (IllegalArgumentException e) {
            log.warn("BookingServiceImpl : getAllBookingsUser FAIL, throw UnsupportedStatusException");
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (reposUser.findById(userId) == null) {
            log.warn("BookingServiceImpl : getAllBookingsUser FAIL, throw IncorrectUserIdException");
            throw new IncorrectUserIdException("Пользователь с id " + userId + " не найден");
        }
        log.info("BookingServiceImpl : getAllBookingsUser, state: " + stateEnum);
        Stream<Booking> stream;
        switch (stateEnum) {
            case CURRENT:
                stream = reposBooking
                        .findAllByBookerForStatusCurrent(userId, LocalDateTime.now())
                        .stream();
                break;
            case PAST:
                stream = reposBooking
                        .findAllByBookerForStatusPast(userId, LocalDateTime.now())
                        .stream();
                break;
            case FUTURE:
                stream = reposBooking
                        .findAllByBookerForStatusFuture(userId, LocalDateTime.now())
                        .stream();
                break;
            case WAITING:
                stream = reposBooking
                        .findAllByBookerForStatusWaitingOrRejected(userId, BookingStatusEnum.WAITING)
                        .stream();
                break;
            case REJECTED:
                stream = reposBooking
                        .findAllByBookerForStatusWaitingOrRejected(userId, BookingStatusEnum.REJECTED)
                        .stream();
                break;
            default:
                stream = reposBooking
                        .findAllByBooker(userId)
                        .stream();
        }
        return stream.map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
    }

    @Override
    public List<BookingOutDto> getAllBookingsItemsUser(long userId, String state) {

        BookingStateEnum stateEnum;
        try {
            stateEnum = Enum.valueOf(BookingStateEnum.class, state);
        } catch (IllegalArgumentException e) {
            log.warn("BookingServiceImpl : getAllBookingsItemUser FAIL, throw UnsupportedStatusException");
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (reposUser.findById(userId) == null) {
            log.warn("BookingServiceImpl : getAllBookingsItemUser FAIL, throw IncorrectUserIdException");
            throw new IncorrectUserIdException("Пользователь с id " + userId + " не найден");
        }
        log.info("BookingServiceImpl : getAllBookingsItemsUser, state: " + stateEnum);
        Stream<Booking> stream;

        switch (stateEnum) {
            case CURRENT:
                stream = reposBooking
                        .findAllBookingsItemsUserForStatusCurrent(userId, LocalDateTime.now())
                        .stream();
                break;
            case PAST:
                stream = reposBooking
                        .findAllBookingsItemsUserForStatusPast(userId, LocalDateTime.now())
                        .stream();
                break;
            case FUTURE:
                stream = reposBooking
                        .findAllBookingsItemsUserForStatusFuture(userId, LocalDateTime.now())
                        .stream();
                break;
            case WAITING:
                stream = reposBooking
                        .findAllBookingsItemsUserForStatusWaitingOrRejected(userId, BookingStatusEnum.WAITING)
                        .stream();
                break;
            case REJECTED:
                stream = reposBooking
                        .findAllBookingsItemsUserForStatusWaitingOrRejected(userId, BookingStatusEnum.REJECTED)
                        .stream();
                break;
            default:
                stream = reposBooking
                        .findAllBookingsItemsUser(userId)
                        .stream();
        }
        return stream.map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
    }
}
