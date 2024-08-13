package ru.practicum.shareit.request.dto.model;

import lombok.*;
import ru.practicum.shareit.item.dto.model.item.ItemToRequestDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Шаблон Request для исходящих данных со списком ответов
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestWithItemDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemToRequestDto> items;
}