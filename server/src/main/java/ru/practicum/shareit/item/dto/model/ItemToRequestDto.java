package ru.practicum.shareit.item.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Item шаблон для передачи ответа на запрос
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemToRequestDto {
    private Long id;
    private String name;
    private Long owner;
}
