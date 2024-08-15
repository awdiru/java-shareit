package ru.practicum.shareit.comment;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Comment DTO шаблон для исходящих ответов
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CommentOutDto {
    private Long id;
    private String authorName;
    private String text;
    private LocalDateTime created;
}