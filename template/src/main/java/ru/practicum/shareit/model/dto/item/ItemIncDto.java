package ru.practicum.shareit.model.dto.item;

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
public class ItemIncDto {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
}