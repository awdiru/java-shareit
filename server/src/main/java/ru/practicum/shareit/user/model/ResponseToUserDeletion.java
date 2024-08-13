package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseToUserDeletion {
    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String path;

    public ResponseToUserDeletion(int status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }
}