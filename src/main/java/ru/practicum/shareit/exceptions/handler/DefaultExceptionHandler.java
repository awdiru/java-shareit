package ru.practicum.shareit.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Шаблон ответа для обработки исключений
 */
public class DefaultExceptionHandler {
    public static ResponseEntity<Response> response(HttpStatus status, String message, String path) {
        Response response = new Response(status.value(), message, path);
        return new ResponseEntity<>(response, status);
    }
}
