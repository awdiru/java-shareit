package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.annotations.BookingControllerExceptionHandler;
import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;

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
    private final String userIdHeader = "X-Sharer-User-Id";


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
     */
    @PostMapping
    BookingOutDto createBooking(@RequestBody @Valid BookingIncDto bookingIncDto,
                                @RequestHeader(userIdHeader) @NotBlank final Long userId) {

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
     */
    @PatchMapping("/{bookingId}")
    BookingOutDto approvedBooking(@RequestHeader(userIdHeader) @NotBlank final Long userId,
                                  @PathVariable Long bookingId,
                                  @RequestParam Boolean approved) {

        log.info("BookingController : approvedBooking");
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    /**
     * Посмотреть запрос на бронирование
     *
     * @param userId    id пользователя
     * @param bookingId id запроса
     * @return запрос на бронирование
     */
    @GetMapping("/{bookingId}")
    BookingOutDto getBooking(@RequestHeader(userIdHeader) @NotBlank final Long userId,
                             @PathVariable Long bookingId) {

        log.info("BookingController : getBookingStatus");
        return bookingService.getBooking(userId, bookingId);
    }

    /**
     * Посмотреть все запросы на бронирования данного пользователя
     *
     * @param userId id пользователя
     * @param state  необязательный параметр, модификатор запроса
     * @return список запросов на бронирование
     */
    @GetMapping
    List<BookingOutDto> getAllBookingsUser(@RequestHeader(userIdHeader) @NotBlank final Long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {

        log.info("BookingController : getAllBookingUser");
        return bookingService.getAllBookingsUser(userId, state);
    }

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     *
     * @param userId идентификатор пользователя
     * @param state  необязательный параметр, модификатор запроса
     * @return список запросов на бронирование данной вещи
     */
    @GetMapping("/owner")
    List<BookingOutDto> getAllBookingsItemsUser(@RequestHeader(userIdHeader) @NotBlank final Long userId,
                                                @RequestParam(defaultValue = "ALL") String state) {

        log.info("BookingController : getAllBookingsItemsUser");
        return bookingService.getAllBookingsItemsUser(userId, state);
    }
}
