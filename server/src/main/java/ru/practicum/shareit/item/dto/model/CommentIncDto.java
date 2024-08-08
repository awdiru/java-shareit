package ru.practicum.shareit.item.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Шаблон Comment для входящих запросов на создание комментария
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentIncDto {
    private String text;
}