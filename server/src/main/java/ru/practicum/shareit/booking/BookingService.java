package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;

import java.util.List;

/**
 * Интерфейс сервиса для BookingController
 */
public interface BookingService {

    /**
     * Создание нового запроса на бронирование
     *
     * @param bookingDto запрос на бронирование
     * @param userId     id пользователя
     * @return созданный запрос на бронирование
     */
    BookingOutDto createBooking(BookingIncDto bookingDto, Long userId);

    /**
     * Подтверждение запроса на бронирование
     *
     * @param userId    id пользователя
     * @param bookingId id вещи
     * @param approved  подтверждение/отклонение запроса
     * @return подтвержденный запрос
     */
    BookingOutDto approvedBooking(Long userId, Long bookingId, Boolean approved);

    /**
     * Получить данные о бронировании
     *
     * @param userId    id пользователя
     * @param bookingId id бронирования
     * @return данные о бронировании
     */
    BookingOutDto getBooking(Long userId, Long bookingId);

    /**
     * Получить данные обо всех бронированиях пользователя
     *
     * @param userId id пользователя
     * @param state  необязательный параметр; модификатор запроса BookingStateEnum
     * @return данные о бронированиях пользователя
     */
    List<BookingOutDto> getAllBookingsUser(Long userId, String state, Integer from, Integer size);

    /**
     * Получить список всех бронирований всех вещей пользователя
     *
     * @param userId Id пользователя
     * @param state  необязательный параметр; модификатор запроса BookingStateEnum
     * @return список всех бронирований всех вещей пользователя
     */
    List<BookingOutDto> getAllBookingsItemsUser(Long userId, String state, Integer from, Integer size);
}
