package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exceptions.IncorrectCommentatorException;
import ru.practicum.shareit.exceptions.IncorrectItemIdException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.exceptions.handler.DefaultExceptionHandler;
import ru.practicum.shareit.exceptions.handler.Response;
import ru.practicum.shareit.item.annotations.ItemControllerExceptionHandler;

/**
 * Обработчик исключений для класса ItemController
 */
@ControllerAdvice(annotations = ItemControllerExceptionHandler.class)
public class ItemExceptionHandler {
    private final String path = "/items";

    @ExceptionHandler(IncorrectItemIdException.class)
    public ResponseEntity<Response> handleIncorrectItemIdException(IncorrectItemIdException e) {
        return DefaultExceptionHandler.response(HttpStatus.NOT_FOUND, e.getMessage(), path);
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

    @ExceptionHandler(IncorrectCommentatorException.class)
    public ResponseEntity<Response> handleIncorrectCommentatorException(IncorrectCommentatorException e) {
        return DefaultExceptionHandler.response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }
}
