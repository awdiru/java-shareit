package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.annotations.BookingControllerExceptionHandler;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.exceptions.handler.DefaultExceptionHandler;
import ru.practicum.shareit.exceptions.handler.Response;

/**
 * Обработчик исключений для класса BookingController
 */
@ControllerAdvice(annotations = BookingControllerExceptionHandler.class)
public class BookingExceptionHandler {
    private final String path = "/bookings";
    @ExceptionHandler(UnsupportedStatusException.class)
    public ResponseEntity<Response> handleUnsupportedStatusException(UnsupportedStatusException e) {
        return DefaultExceptionHandler.response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), path);
    }

    @ExceptionHandler(IncorrectItemIdException.class)
    public ResponseEntity<Response> handleIncorrectItemIdException(IncorrectItemIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }

    @ExceptionHandler(ItemAvailableException.class)
    public ResponseEntity<Response> handleItemAvailableException(ItemAvailableException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }

    @ExceptionHandler(IncorrectBookingTimeException.class)
    public ResponseEntity<Response> handleIncorrectBookingTimeException(IncorrectBookingTimeException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }

    @ExceptionHandler(IncorrectUserIdException.class)
    public ResponseEntity<Response> handleIncorrectUserIdException(IncorrectUserIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, "Передано невалидное значение "
                + e.getFieldError().getField(), path);
    }

    @ExceptionHandler(IncorrectBookingIdException.class)
    public ResponseEntity<Response> handleIncorrectBookingIdException(IncorrectBookingIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }

    @ExceptionHandler(FailApprovedBookingException.class)
    public ResponseEntity<Response> handleFailApprovedBookingException(FailApprovedBookingException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }

    @ExceptionHandler(FailCreateBookingOwnerItem.class)
    public ResponseEntity<Response> handleFailCreateBookingOwnerItem(FailCreateBookingOwnerItem e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }
}
