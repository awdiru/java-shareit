package ru.practicum.shareit.item.dto.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Шаблон исходящих запросов на создание комментария
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentOutDto {
    private Long id;
    private String authorName;
    private String text;
    private LocalDateTime created;
}