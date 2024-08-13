package ru.practicum.shareit.item.dto.model;

import lombok.*;

/**
 * Item шаблон для передачи ответа на запрос
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemToRequestDto {
    private Long id;
    private String name;
    private Long owner;
}
