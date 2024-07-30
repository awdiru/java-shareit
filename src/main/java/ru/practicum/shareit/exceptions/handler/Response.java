package ru.practicum.shareit.exceptions.handler;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Шаблон объекта для ответа при обработке исключений
 */
@Getter
@Setter
public class Response {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String path;

    public Response(int status, String error, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
    }
}
