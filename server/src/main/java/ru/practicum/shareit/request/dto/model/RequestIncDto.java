package ru.practicum.shareit.request.dto.model;

import lombok.*;

/**
 * Шаблон Request на создание вещи для входящих данных
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class RequestIncDto {
    private Long id;
    private String description;
}
