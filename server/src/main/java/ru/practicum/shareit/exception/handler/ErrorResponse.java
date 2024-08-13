package ru.practicum.shareit.exception.handler;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Шаблон объекта для ответа при обработке исключений
 */
@Getter
@Setter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String path;

    public ErrorResponse(int status, String error, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
    }
}