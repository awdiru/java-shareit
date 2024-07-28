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
     * @return запрос на бронирование
     */
    BookingOutDto createBooking(BookingIncDto bookingDto, Long userId)
            throws ItemAvailableException, IncorrectItemIdException,
            IncorrectBookingTimeException, IncorrectUserIdException,
            FailCreateBookingOwnerItem;

    /**
     * Подтверждение запроса на бронирование
     *
     * @param userId    идентификатор владельца
     * @param bookingId идентификатор запроса
     * @param approved  подтверждение (true/false)
     * @return измененный запрос на бронирование
     */
    BookingOutDto approvedBooking(Long userId, Long bookingId, Boolean approved)
            throws IncorrectUserIdException, FailApprovedBookingException, IncorrectBookingIdException;

    /**
     * Получение информации о бронировании
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @return запрашиваемое бронирование
     */
    BookingOutDto getBooking(Long userId, Long bookingId) throws IncorrectUserIdException, IncorrectBookingIdException;

    /**
     * Получение информации о всех бронированиях пользователя
     *
     * @param userId идентификатор пользователя
     * @param state  необязательный параметр; статус бронирования BookingStateEnum
     * @return список бронирований пользователя
     */
    List<BookingOutDto> getAllBookingsUser(Long userId, BookingStateEnum state) throws IncorrectUserIdException;

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     *
     * @param userId идентификатор пользователя
     * @param state  необязательный параметр; статус бронирования BookingStateEnum
     * @return список бронирований для всех вещей текущего пользователя
     */
    List<BookingOutDto> getAllBookingsItemUser(Long userId, BookingStateEnum state) throws IncorrectUserIdException;
}
