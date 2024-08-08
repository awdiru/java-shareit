package ru.practicum.shareit.item.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Шаблон исходящих запросов на создание комментария
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentOutDto {
    private Long id;
    private String authorName;
    private String text;
    private LocalDateTime created;
}