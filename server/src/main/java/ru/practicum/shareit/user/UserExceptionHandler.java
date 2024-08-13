package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.DataException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.exception.handler.DefaultExceptionHandler;
import ru.practicum.shareit.exception.handler.ErrorResponse;
import ru.practicum.shareit.user.annotations.UserControllerExceptionHandler;

/**
 * Обработчик исключений для класса UserController
 */
@ControllerAdvice(annotations = UserControllerExceptionHandler.class)
public class UserExceptionHandler {
    private final String path = "/users";

    @ExceptionHandler(IncorrectUserIdException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectUserIdException(IncorrectUserIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, "Передано невалидное значение "
                + e.getFieldError().getField(), path);
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ErrorResponse> handleDataException(DataException e) {
        return DefaultExceptionHandler.response(HttpStatus.CONFLICT, e.getMessage(), path);
    }
}
