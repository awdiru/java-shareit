package ru.practicum.shareit.model.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Item DTO шаблон для входящих запросов
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(description = "Сущность вещи")
public class ItemIncDto {
    @NotNull
    @Schema(description = "Название вещи",defaultValue = "Крестовая отвертка для Ивана")
    private String name;
    @NotNull
    @Schema(description = "Описание вещи",defaultValue = "Лучшая крестовая отвертка в мире!")
    private String description;
    @NotNull
    @Schema(description = "Доступность к бронированию", defaultValue = "true")
    private Boolean available;
    @Schema(description = "id запроса, по которому вещь была создана. Опционально", defaultValue = "1")
    private Long requestId;
}