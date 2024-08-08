package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private final String USER_ID_HEAD = "X-Sharer-User-Id";

    /**
     * Создание запроса на бронирование вещи
     *
     * @param bookingDto входящий Booking
     * @param userId     id пользователя
     * @return ответ сервера
     */
    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody @Valid final BookingDto bookingDto,
                                                @RequestHeader(USER_ID_HEAD) final Long userId) {

        log.info("Post booking; userId={}, itemId={}", userId, bookingDto.getItemId());
        return bookingClient.createBooking(userId, bookingDto);
    }

    /**
     * Подтверждение бронирования
     *
     * @param userId    id пользователя
     * @param bookingId id запроса
     * @param approved  подтверждение/отклонение запроса
     * @return ответ сервера
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approvedBooking(@RequestHeader(USER_ID_HEAD) final Long userId,
                                                  @PathVariable final Long bookingId,
                                                  @RequestParam final Boolean approved) {

        log.info("Patch booking; userId={}, approved={}", userId, approved);
        return bookingClient.approvedBooking(userId, bookingId, approved);
    }

    /**
     * Посмотреть запрос на бронирование
     *
     * @param userId    id пользователя
     * @param bookingId id запроса
     * @return ответ сервера
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID_HEAD) final Long userId,
                                             @PathVariable final Long bookingId) {
        log.info("Get booking; {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    /**
     * Посмотреть все запросы на бронирования данного пользователя
     *
     * @param userId     id пользователя
     * @param stateParam необязательный параметр, модификатор запроса
     * @param from       индекс страницы
     * @param size       размер страницы
     * @return ответ сервера
     */
    @GetMapping
    public ResponseEntity<Object> getAllBookingsUser(@RequestHeader(USER_ID_HEAD) final Long userId,
                                                     @RequestParam(name = "state", defaultValue = "all") final String stateParam,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") final Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") final Integer size) {

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        log.info("Get bookings user; state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookingsUser(userId, state, from, size);
    }

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     *
     * @param userId     идентификатор пользователя
     * @param stateParam необязательный параметр, модификатор запроса
     * @param from       индекс страницы
     * @param size       размер страницы
     * @return ответ сервера
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsItemsUser(@RequestHeader(USER_ID_HEAD) final Long userId,
                                                          @RequestParam(name = "state", defaultValue = "all") final String stateParam,
                                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") final Integer from,
                                                          @Positive @RequestParam(name = "size", defaultValue = "10") final Integer size) {

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        log.info("Get bookings owner items user; state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookingsItemsUser(userId, state, from, size);
    }
}