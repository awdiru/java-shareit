package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exceptions.DataException;
import ru.practicum.shareit.exceptions.FailEmailException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.exceptions.handler.DefaultExceptionHandler;
import ru.practicum.shareit.exceptions.handler.Response;
import ru.practicum.shareit.user.annotations.UserControllerExceptionHandler;

/**
 * Обработчик исключений для класса UserController
 */
@ControllerAdvice(annotations = UserControllerExceptionHandler.class)
public class UserExceptionHandler {
    private final String path = "/users";

    @ExceptionHandler(IncorrectUserIdException.class)
    public ResponseEntity<Response> handleIncorrectUserIdException(IncorrectUserIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }

    @ExceptionHandler(FailEmailException.class)
    public ResponseEntity<Response> handleFailEmailException(FailEmailException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, "Передано невалидное значение "
                + e.getFieldError().getField(), path);
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<Response> handleDataException(DataException e) {
        return DefaultExceptionHandler.response(HttpStatus.CONFLICT, e.getMessage(), path);
    }
}
