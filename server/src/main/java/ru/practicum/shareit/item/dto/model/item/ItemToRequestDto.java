package ru.practicum.shareit.item.dto.model.item;

/**
 * Item шаблон для передачи ответа на запрос
 */
public interface ItemToRequestDto {
    Long getId();

    String getName();

    Long getOwnerId();
}