package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Шаблон Request на создание вещи для передачи данных
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class RequestDto {
    private Long id;
    @NotBlank
    private String description;
}