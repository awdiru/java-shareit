package ru.practicum.shareit.model.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


/**
 * Comment DTO шаблон для входящих запросов
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CommentIncDto {
    @NotBlank
    private String text;
}