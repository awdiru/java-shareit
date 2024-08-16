package ru.practicum.shareit.model.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Сущность комментария")
public class CommentIncDto {
    @NotBlank
    @Schema(description = "Текст комментария", defaultValue = "Отвертка действительно класс!")
    private String text;
}