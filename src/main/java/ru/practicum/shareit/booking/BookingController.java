package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.annotations.BookingControllerExceptionHandler;
import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;
import ru.practicum.shareit.booking.enums.BookingStateEnum;
import ru.practicum.shareit.exceptions.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * RestController для работы приложения по пути /bookings
 */
@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@BookingControllerExceptionHandler
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Создание запроса на бронирование вещи
     *
     * @param bookingIncDto входящий Booking
     * @param userId        id пользователя
     * @return созданный запрос на бронирование
     * @throws IncorrectUserIdException      некорректный id пользователя, пользователь не найден
     * @throws IncorrectItemIdException      некорректный id вещи, вещь не найдена
     * @throws ItemAvailableException        вещь не доступна для бронирования
     * @throws IncorrectBookingTimeException некорректные временный рамки запроса
     * @throws FailCreateBookingOwnerItem    попытка создания запроса владельцем вещи
     */
    @PostMapping
    BookingOutDto createBooking(@RequestBody @Valid BookingIncDto bookingIncDto,
                                @RequestHeader("X-Sharer-User-Id") @NotBlank final long userId)
            throws IncorrectUserIdException, IncorrectItemIdException,
            ItemAvailableException, IncorrectBookingTimeException, FailCreateBookingOwnerItem {

        log.info("BookingController : createBooking");
        return bookingService.createBooking(bookingIncDto, userId);

    }

    /**
     * Подтверждение бронирования
     *
     * @param userId    id пользователя
     * @param bookingId id запроса
     * @param approved  подтверждение/отклонение запроса
     * @return подтвержденный/отклоненный запрос
     * @throws FailApprovedBookingException запрос уже был подтвержден ранее
     * @throws IncorrectBookingIdException  некорректный id запроса
     * @throws IncorrectOwnerIdException    некорректный id владельца
     * @throws IncorrectUserIdException     некорректный id пользователя
     */
    @PatchMapping("/{bookingId}")
    BookingOutDto approvedBooking(@RequestHeader("X-Sharer-User-Id") @NotBlank final long userId,
                                  @PathVariable Long bookingId,
                                  @RequestParam Boolean approved)
            throws FailApprovedBookingException, IncorrectBookingIdException, IncorrectOwnerIdException, IncorrectUserIdException {

        log.info("BookingController : approvedBooking");

        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    /**
     * Посмотреть запрос на бронирование
     *
     * @param userId    id пользователя
     * @param bookingId id запроса
     * @return запрос на бронирование
     * @throws IncorrectUserIdException    некорректный id пользователя
     * @throws IncorrectBookingIdException некорректный id запроса
     */
    @GetMapping("/{bookingId}")
    BookingOutDto getBooking(@RequestHeader("X-Sharer-User-Id") @NotBlank final long userId,
                             @PathVariable Long bookingId)
            throws IncorrectUserIdException, IncorrectBookingIdException {
        log.info("BookingController : getBookingStatus");
        return bookingService.getBooking(userId, bookingId);

    }

    /**
     * Посмотреть все запросы на бронирования данного пользователя
     *
     * @param userId id пользователя
     * @param state  необязательный параметр, модификатор запроса
     * @return список запросов на бронирование
     * @throws UnsupportedStatusException некорректный модификатор запроса
     * @throws IncorrectUserIdException   некорректный id пользователя
     */
    @GetMapping
    List<BookingOutDto> getAllBookingsUser(@RequestHeader("X-Sharer-User-Id") @NotBlank final long userId,
                                           @RequestParam(required = false) String state)
            throws UnsupportedStatusException, IncorrectUserIdException {
        log.info("BookingController : getAllBookingUser");

        try {
            BookingStateEnum stateEnum;
            if (state == null)
                stateEnum = BookingStateEnum.ALL;
            else
                stateEnum = Enum.valueOf(BookingStateEnum.class, state);
            return bookingService.getAllBookingsUser(userId, stateEnum);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     *
     * @param userId идентификатор пользователя
     * @param state  необязательный параметр, модификатор запроса
     * @return список запросов на бронирование данной вещи
     * @throws IncorrectUserIdException   некорректный id пользователя
     * @throws UnsupportedStatusException некорректный модификатор запроса
     */
    @GetMapping("/owner")
    List<BookingOutDto> getAllBookingsItemsUser(@RequestHeader("X-Sharer-User-Id") @NotBlank final long userId,
                                                @RequestParam(required = false) String state)
            throws IncorrectUserIdException, UnsupportedStatusException {
        try {
            BookingStateEnum stateEnum;
            if (state == null)
                stateEnum = BookingStateEnum.ALL;
            else
                stateEnum = Enum.valueOf(BookingStateEnum.class, state);
            return bookingService.getAllBookingsItemUser(userId, stateEnum);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
