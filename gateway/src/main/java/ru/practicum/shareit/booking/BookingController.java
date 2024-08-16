package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.model.dto.booking.BookingIncDto;
import ru.practicum.shareit.model.enums.BookingStateEnum;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.practicum.shareit.constants.Headers.USER_ID_HEADER;

/**
 * RestController для работы приложения по пути /bookings
 */
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Бронирование")
public class BookingController {
    private final BookingClient bookingClient;

    @Operation(summary = "Создать новое бронирование",
            description = """
                    Создать запрос на бронирование вещи.
                    Владелец не может бронировать свою вещь,
                    нельзя забронировать несуществующую вещь,
                    нельзя забронировать в прошлом,
                    нельзя поставить время окончания раньше времени брони
                    """)
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createBooking(@Parameter(description = "id пользователя")
                                                @RequestHeader(USER_ID_HEADER) final Long userId,
                                                @RequestBody @Valid final BookingIncDto bookingDto) {

        log.info("Post booking; userId={}, itemId={}", userId, bookingDto.getItemId());
        return bookingClient.createBooking(userId, bookingDto);
    }

    @Operation(summary = "Подтвердить или отменить бронирование",
            description = """
                    Подтвердить или отклонить бронирование может только владелец вещи.
                    Отменить бронирование может только автор бронирования.
                    Сторонний пользователь не может взаимодействовать с бронированием
                    """)
    @PatchMapping(value = "/{bookingId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> approvedBooking(@Parameter(description = "id пользователя")
                                                  @RequestHeader(USER_ID_HEADER) final Long userId,
                                                  @Parameter(description = "id бронирования")
                                                  @PathVariable final Long bookingId,
                                                  @Parameter(description = "Подтверждение бронирования")
                                                  @RequestParam final Boolean approved) {

        log.info("Patch booking; userId={}, bookingId={}, approved={}", userId, bookingId, approved);
        return bookingClient.approvedBooking(userId, bookingId, approved);
    }

    @Operation(summary = "Посмотреть запрос на бронирование",
            description = """
                    Посмотреть запрос на бронирование может либо автор бронирования, либо владелец вещи.
                    Сторонний пользователь не может взаимодействовать с бронированием
                    """)
    @GetMapping(value = "/{bookingId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBooking(@Parameter(description = "id пользователя")
                                             @RequestHeader(USER_ID_HEADER) final Long userId,
                                             @Parameter(description = "id бронирования")
                                             @PathVariable final Long bookingId) {
        log.info("Get booking; bookingId={}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @Operation(summary = "Посмотреть все запросы на бронирование",
            description = """
                    Вернуть страницу № from размера size с запросами на бронирование пользователя.
                    Модификатор запроса stateParam определяет фильтрацию бронирований
                    """)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllBookingsUser(@Parameter(description = "id пользователя")
                                                     @RequestHeader(USER_ID_HEADER) final Long userId,
                                                     @Parameter(description = "Модификатор фильтрации запроса")
                                                     @RequestParam(name = "state", defaultValue = "all") final String stateParam,
                                                     @Parameter(description = "№ страницы")
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") final Integer from,
                                                     @Parameter(description = "Размер страницы")
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") final Integer size) {

        BookingStateEnum state = BookingStateEnum.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        log.info("Get bookings user; state={}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookingsUser(userId, state, from, size);
    }

    @Operation(summary = "Вернуть все запросы на бронирование вещей пользователя",
            description = """
                    Вернуть страницу № from размера size со всеми запросами на бронирование вещей пользователя.
                    Модификатор запроса stateParam определяет фильтрацию бронирований
                    """)
    @GetMapping(value = "/owner", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllBookingsItemsUser(@Parameter(description = "id пользователя")
                                                          @RequestHeader(USER_ID_HEADER) final Long userId,
                                                          @Parameter(description = "Модификатор фильтрации запроса")
                                                          @RequestParam(name = "state", defaultValue = "all") final String stateParam,
                                                          @Parameter(description = "№ страницы")
                                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") final Integer from,
                                                          @Parameter(description = "Размер страницы")
                                                          @Positive @RequestParam(name = "size", defaultValue = "10") final Integer size) {

        BookingStateEnum state = BookingStateEnum.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        log.info("Get bookings owner items user; state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookingsItemsUser(userId, state, from, size);
    }
}