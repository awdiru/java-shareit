package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String text;
}