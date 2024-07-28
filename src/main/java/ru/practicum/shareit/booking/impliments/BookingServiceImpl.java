package ru.practicum.shareit.booking.impliments;

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

/**
 * Реализация сервиса для BookingController
 */
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository reposBooking;
    private final UserRepository reposUser;
    private final ItemRepository reposItem;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(final BookingRepository bookingRepository, ItemRepository reposItem, UserRepository repsUser, BookingMapper bookingMapper) {
        this.reposBooking = bookingRepository;
        this.reposUser = repsUser;
        this.reposItem = reposItem;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingOutDto createBooking(BookingIncDto bookingIncDto, Long userId)
            throws ItemAvailableException, IncorrectItemIdException, IncorrectBookingTimeException, IncorrectUserIdException, FailCreateBookingOwnerItem {

        Booking booking = bookingMapper.toBookingFromBookingIncDto(bookingIncDto, userId);

        if (reposUser.findById((long) userId) == null)
            throw new IncorrectUserIdException("Некорректный id пользователя");

        if (booking.getItem() == null)
            throw new IncorrectItemIdException(
                    "Предмет с id " + bookingIncDto.getItemId() + " не найден");

        if (!booking.getItem().getAvailable())
            throw new ItemAvailableException(
                    "Предмет с id " + booking.getItem().getId() + " не доступен для бронирования");

        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().equals(booking.getStart()))
            throw new IncorrectBookingTimeException("Время окончания бронирования не может быть раньше или равно " +
                    "времени начала бронирования");

        if (booking.getEnd().isBefore(LocalDateTime.now()))
            throw new IncorrectBookingTimeException("Время окончания бронирования не может быть в прошлом");

        if (booking.getStart().isBefore(LocalDateTime.now()))
            throw new IncorrectBookingTimeException("Время начала бронирования не может быть в прошлом");

        if (booking.getItem().getOwner().getId().equals(userId))
            throw new FailCreateBookingOwnerItem("Владелец вещи не может ее забронировать");

        booking.setBooker(reposUser.findById((long) userId));
        booking.setStatus(BookingStatusEnum.WAITING);
        booking = reposBooking.save(booking);
        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public BookingOutDto approvedBooking(Long userId, Long bookingId, Boolean approved)
            throws FailApprovedBookingException, IncorrectBookingIdException, IncorrectOwnerIdException, IncorrectUserIdException {
        Booking booking = reposBooking.findById((long) bookingId);
        if (booking == null)
            throw new IncorrectBookingIdException("Бронирование с id " + bookingId + " не найдено");

        if ((long) userId == booking.getBooker().getId() && approved)
            throw new IncorrectUserIdException("Бронирование может подтвердить только владелец вещи");

        if ((long) userId != booking.getItem().getOwner().getId())
            throw new IncorrectOwnerIdException("Бронирование может подтвердить только владелец вещи");

        if (booking.getStatus().equals(BookingStatusEnum.APPROVED) || booking.getStatus().equals(BookingStatusEnum.REJECTED))
            throw new FailApprovedBookingException("Подтверждение бронирования уже произошло. Статус бронирования: " + booking.getStatus());

        if (booking.getStatus().equals(BookingStatusEnum.CANCELED))
            throw new FailApprovedBookingException("Бронирование отменено пользователем");

        if (approved) {
            booking.setStatus(BookingStatusEnum.APPROVED);
            Item item = booking.getItem();

            if (item.getNumberOfRentals() != null)
                item.setNumberOfRentals(item.getNumberOfRentals() + 1);
            else item.setNumberOfRentals(1);

            reposItem.save(item);

        } else if ((long) userId == booking.getBooker().getId())
            booking.setStatus(BookingStatusEnum.CANCELED);

        else booking.setStatus(BookingStatusEnum.REJECTED);

        reposBooking.save(booking);
        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public BookingOutDto getBooking(Long userId, Long bookingId) throws IncorrectUserIdException, IncorrectBookingIdException {

        Booking booking = reposBooking.findById((long) bookingId);
        if (booking == null)
            throw new IncorrectBookingIdException("Бронирование с id " + bookingId + " не найдено");

        if (!(booking.getItem().getOwner().getId() == (long) userId || booking.getBooker().getId() == (long) userId))
            throw new IncorrectUserIdException("Получение данных о конкретном бронировании " +
                    "может быть выполнено либо автором бронирования, либо владельцем вещи");

        return bookingMapper.toBookingOutDtoFromBooking(booking);
    }

    @Override
    public List<BookingOutDto> getAllBookingsUser(Long userId, BookingStateEnum state) throws IncorrectUserIdException {
        if (reposUser.findById(userId).isEmpty())
            throw new IncorrectUserIdException("Пользователь с id " + userId + " не найден");

        switch (state) {
            case CURRENT:
                return reposBooking.findAllByBookerForStatusCurrent(userId, LocalDateTime.now()).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            case PAST:
                return reposBooking.findAllByBookerForStatusPast(userId, LocalDateTime.now()).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            case FUTURE:
                return reposBooking.findAllByBookerForStatusFuture(userId, LocalDateTime.now()).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            case WAITING:
                return reposBooking.findAllByBookerForStatusWaitingAndRejected(userId,
                                BookingStatusEnum.WAITING).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            case REJECTED:
                return reposBooking.findAllByBookerForStatusWaitingAndRejected(userId,
                                BookingStatusEnum.REJECTED).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            default:
                return reposBooking.findAllByBooker(userId).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
        }
    }

    @Override
    public List<BookingOutDto> getAllBookingsItemUser(Long userId, BookingStateEnum state) throws IncorrectUserIdException {
        if (reposUser.findById(userId).isEmpty())
            throw new IncorrectUserIdException("Пользователь с id " + userId + " не найден");
        switch (state) {
            case CURRENT:
                return reposBooking.findAllBookingsItemsUserForStatusCurrent(userId, LocalDateTime.now()).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            case PAST:
                return reposBooking.findAllBookingsItemsUserForStatusPast(userId, LocalDateTime.now()).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            case FUTURE:
                return reposBooking.findAllBookingsItemsUserForStatusFuture(userId, LocalDateTime.now()).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            case WAITING:
                return reposBooking.findAllBookingsItemsUserForStatusWaitingOrRejected(userId,
                                BookingStatusEnum.WAITING).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            case REJECTED:
                return reposBooking.findAllBookingsItemsUserForStatusWaitingOrRejected(userId,
                                BookingStatusEnum.REJECTED).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
            default:
                return reposBooking.findAllBookingsItemsUser(userId).stream()
                        .map(bookingMapper::toBookingOutDtoFromBooking).collect(Collectors.toList());
        }
    }
}
