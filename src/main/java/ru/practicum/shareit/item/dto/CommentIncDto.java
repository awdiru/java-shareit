package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

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
