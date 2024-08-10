package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.IncorrectCommentatorException;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.exception.IncorrectRequestIdException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.exception.handler.DefaultExceptionHandler;
import ru.practicum.shareit.exception.handler.ErrorResponse;
import ru.practicum.shareit.item.annotations.ItemControllerExceptionHandler;

/**
 * Обработчик исключений для класса ItemController
 */
@ControllerAdvice(annotations = ItemControllerExceptionHandler.class)
public class ItemExceptionHandler {
    private final String path = "/items";

    @ExceptionHandler(IncorrectItemIdException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectItemIdException(IncorrectItemIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
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

    @ExceptionHandler(IncorrectCommentatorException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectCommentatorException(IncorrectCommentatorException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }

    @ExceptionHandler(IncorrectRequestIdException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectRequestIdException(IncorrectRequestIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
    }
}
