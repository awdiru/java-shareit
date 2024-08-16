package ru.practicum.shareit.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request DTO шаблон для входящих запросов
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(description = "Сущность запроса")
public class RequestIncDto {
    @Schema(description = "id запроса")
    private Long id;
    @NotBlank
    @Schema(description = "Описание запроса", defaultValue = "Срочно требуется крестовая отвертка!")
    private String description;
}