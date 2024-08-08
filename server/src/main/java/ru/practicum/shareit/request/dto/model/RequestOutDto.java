package ru.practicum.shareit.request.dto.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Шаблон Request для исходящих данных
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class RequestOutDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}
