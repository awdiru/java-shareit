package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.annotations.BookingControllerExceptionHandler;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.exception.handler.DefaultExceptionHandler;
import ru.practicum.shareit.exception.handler.ErrorResponse;

/**
 * Обработчик исключений для класса BookingController
 */
@ControllerAdvice(annotations = BookingControllerExceptionHandler.class)
public class BookingExceptionHandler {
    private final String path = "/bookings";

    @ExceptionHandler(UnsupportedStatusException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedStatusException(UnsupportedStatusException e) {
        return DefaultExceptionHandler.response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), path);
    }

    @ExceptionHandler(IncorrectItemIdException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectItemIdException(IncorrectItemIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }

    @ExceptionHandler(ItemAvailableException.class)
    public ResponseEntity<ErrorResponse> handleItemAvailableException(ItemAvailableException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }

    @ExceptionHandler(IncorrectBookingTimeException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectBookingTimeException(IncorrectBookingTimeException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }

    @ExceptionHandler(IncorrectUserIdException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectUserIdException(IncorrectUserIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, "Передано невалидное значение "
                + e.getFieldError().getField(), path);
    }

    @ExceptionHandler(IncorrectBookingIdException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectBookingIdException(IncorrectBookingIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }

    @ExceptionHandler(FailApprovedBookingException.class)
    public ResponseEntity<ErrorResponse> handleFailApprovedBookingException(FailApprovedBookingException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }

    @ExceptionHandler(FailCreateBookingOwnerItem.class)
    public ResponseEntity<ErrorResponse> handleFailCreateBookingOwnerItem(FailCreateBookingOwnerItem e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }

    @ExceptionHandler(IncorrectOwnerIdException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectOwnerIdException(IncorrectOwnerIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }
}