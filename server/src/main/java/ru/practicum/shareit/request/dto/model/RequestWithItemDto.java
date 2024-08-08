package ru.practicum.shareit.request.dto.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.model.ItemToRequestDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Шаблон Request для исходящих данных со списком ответов
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class RequestWithItemDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemToRequestDto> items;
}
