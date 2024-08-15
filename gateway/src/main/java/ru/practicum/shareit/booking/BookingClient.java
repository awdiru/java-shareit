package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.enums.BookingStateEnum;

import java.util.Map;

/**
 * Клиент приложения по пути /bookings
 */
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создание запроса на бронирование вещи
     *
     * @param bookingDto входящий Booking
     * @param userId     id пользователя
     * @return ответ сервера
     */
    public ResponseEntity<Object> createBooking(final Long userId,
                                                final BookingIncDto bookingDto) {

        return post("", userId, bookingDto);
    }

    /**
     * Подтверждение бронирования
     *
     * @param userId    id пользователя
     * @param bookingId id запроса
     * @param approved  подтверждение/отклонение запроса
     * @return ответ сервера
     */
    public ResponseEntity<Object> approvedBooking(final Long userId,
                                                  final Long bookingId,
                                                  final Boolean approved) {

        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    /**
     * Посмотреть запрос на бронирование
     *
     * @param userId    id пользователя
     * @param bookingId id запроса
     * @return ответ сервера
     */
    public ResponseEntity<Object> getBooking(final Long userId,
                                             final Long bookingId) {
        return get("/" + bookingId, userId);
    }

    /**
     * Посмотреть все запросы на бронирования данного пользователя
     *
     * @param userId id пользователя
     * @param state  необязательный параметр, модификатор запроса
     * @param from   индекс страницы
     * @param size   размер страницы
     * @return ответ сервера
     */
    public ResponseEntity<Object> getAllBookingsUser(final Long userId,
                                                     final BookingStateEnum state,
                                                     final Integer from,
                                                     final Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     *
     * @param userId идентификатор пользователя
     * @param state  необязательный параметр, модификатор запроса
     * @param from   индекс страницы
     * @param size   размер страницы
     * @return ответ сервера
     */
    public ResponseEntity<Object> getAllBookingsItemsUser(final Long userId,
                                                          final BookingStateEnum state,
                                                          final Integer from,
                                                          final Integer size) {

        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}