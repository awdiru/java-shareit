package ru.practicum.shareit.request.dto.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

/**
 * Шаблон Request на создание вещи для входящих данных
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class RequestIncDto {
    private Long id;
    private String description;
}
