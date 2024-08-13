package ru.practicum.shareit.request;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.IncorrectRequestIdException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.exception.handler.DefaultExceptionHandler;
import ru.practicum.shareit.exception.handler.ErrorResponse;
import ru.practicum.shareit.request.annotations.RequestControllerExceptionHandler;

/**
 * Обработчик исключений для класса RequestController
 */
@ControllerAdvice(annotations = RequestControllerExceptionHandler.class)
public class RequestExceptionHandler {
    private final String path = "/requests";

    @ExceptionHandler(IncorrectUserIdException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectUserIdException(IncorrectUserIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }

    @ExceptionHandler(IncorrectRequestIdException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectUserIdException(IncorrectRequestIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }
}