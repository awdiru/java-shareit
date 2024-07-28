package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.model.BookingIncDto;
import ru.practicum.shareit.booking.dto.model.BookingOutDto;
import ru.practicum.shareit.booking.enums.BookingStateEnum;
import ru.practicum.shareit.exceptions.*;

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
     * @throws ItemAvailableException        при попытке забронировать вещь, недоступную для бронирования
     * @throws IncorrectItemIdException      некорректный id вещи
     * @throws IncorrectBookingTimeException некорректные временные рамки запроса
     * @throws IncorrectUserIdException      некорректный id пользователя
     * @throws FailCreateBookingOwnerItem    при попытке создать запрос владельцем вещи
     */
    BookingOutDto createBooking(BookingIncDto bookingDto, Long userId)
            throws ItemAvailableException, IncorrectItemIdException,
            IncorrectBookingTimeException, IncorrectUserIdException,
            FailCreateBookingOwnerItem;

    /**
     * Подтверждение запроса на бронирование
     *
     * @param userId    id пользователя
     * @param bookingId id вещи
     * @param approved  подтверждение/отклонение запроса
     * @return подтвержденный запрос
     * @throws FailApprovedBookingException подтверждение уже произошло ранее
     * @throws IncorrectBookingIdException  некорректный id вещи
     * @throws IncorrectOwnerIdException    при попытке подтвердить запрос не владельцем вещи
     * @throws IncorrectUserIdException     некорректный id пользователя
     */
    BookingOutDto approvedBooking(Long userId, Long bookingId, Boolean approved)
            throws FailApprovedBookingException, IncorrectBookingIdException, IncorrectOwnerIdException, IncorrectUserIdException;

    /**
     * Получить данные о бронировании
     *
     * @param userId    id пользователя
     * @param bookingId id бронирования
     * @return данные о бронировании
     * @throws IncorrectUserIdException    некорректный id пользователя
     * @throws IncorrectBookingIdException некорректный id бронирования
     */
    BookingOutDto getBooking(Long userId, Long bookingId) throws IncorrectUserIdException, IncorrectBookingIdException;

    /**
     * Получить данные обо всех бронированиях пользователя
     *
     * @param userId id пользователя
     * @param state  необязательный параметр; модификатор запроса BookingStateEnum
     * @return данные о бронированиях пользователя
     * @throws IncorrectUserIdException некорректный Id пользователя
     */
    List<BookingOutDto> getAllBookingsUser(Long userId, BookingStateEnum state) throws IncorrectUserIdException;

    /**
     * Получить список всех бронирований всех вещей пользователя
     *
     * @param userId Id пользователя
     * @param state  необязательный параметр; модификатор запроса BookingStateEnum
     * @return список всех бронирований всех вещей пользователя
     * @throws IncorrectUserIdException некорректный id пользователя
     */
    List<BookingOutDto> getAllBookingsItemUser(Long userId, BookingStateEnum state) throws IncorrectUserIdException;
}
