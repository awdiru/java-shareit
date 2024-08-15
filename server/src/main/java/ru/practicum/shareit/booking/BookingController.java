package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.annotations.BookingControllerExceptionHandler;

import java.util.List;

/**
 * RestController для работы приложения по пути /bookings
 */
@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@BookingControllerExceptionHandler
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final String userIdHead = "X-Sharer-User-Id";


    /**
     * Создание запроса на бронирование вещи
     *
     * @param bookingIncDto входящий Booking
     * @param userId        id пользователя
     * @return созданный запрос на бронирование
     */
    @PostMapping
    public BookingOutDto createBooking(@RequestBody final BookingIncDto bookingIncDto,
                                       @RequestHeader(userIdHead) final Long userId) {

        log.info("Post booking; userId={}, itemId={}", userId, bookingIncDto.getItemId());
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
    public BookingOutDto approvedBooking(@RequestHeader(userIdHead) final Long userId,
                                         @PathVariable final Long bookingId,
                                         @RequestParam final Boolean approved) {

        log.info("Patch booking; userId={}, bookingId={}, approved={}", userId, bookingId, approved);
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
    public BookingOutDto getBooking(@RequestHeader(userIdHead) final Long userId,
                                    @PathVariable final Long bookingId) {

        log.info("Get booking; bookingId={}, userId={}", bookingId, userId);
        return bookingService.getBooking(userId, bookingId);
    }

    /**
     * Посмотреть все запросы на бронирования данного пользователя
     *
     * @param userId id пользователя
     * @param state  необязательный параметр, модификатор запроса
     * @param from   индекс страницы
     * @param size   размер страницы
     * @return список запросов на бронирование
     */
    @GetMapping
    public List<BookingOutDto> getAllBookingsUser(@RequestHeader(userIdHead) final Long userId,
                                                  @RequestParam final String state,
                                                  @RequestParam final Integer from,
                                                  @RequestParam final Integer size) {

        log.info("Get bookings user; state={}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingService.getAllBookingsUser(userId, state, from, size);
    }

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     *
     * @param userId идентификатор пользователя
     * @param state  необязательный параметр, модификатор запроса
     * @param from   индекс страницы
     * @param size   размер страницы
     * @return список запросов на бронирование данной вещи
     */
    @GetMapping("/owner")
    public List<BookingOutDto> getAllBookingsItemsUser(@RequestHeader(userIdHead) final Long userId,
                                                       @RequestParam final String state,
                                                       @RequestParam final Integer from,
                                                       @RequestParam final Integer size) {

        log.info("Get bookings owner items user; state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingService.getAllBookingsItemsUser(userId, state, from, size);
    }
}