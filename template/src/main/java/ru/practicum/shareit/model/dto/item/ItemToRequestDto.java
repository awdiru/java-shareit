package ru.practicum.shareit.model.dto.item;

/**
 * Item DTO шаблон для передачи ответа на запрос
 */
public interface ItemToRequestDto {
    Long getId();

    String getName();

    Long getOwnerId();
}