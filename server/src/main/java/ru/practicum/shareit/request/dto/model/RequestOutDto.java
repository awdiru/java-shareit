package ru.practicum.shareit.request.dto.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Шаблон Request для исходящих данных
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class RequestOutDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}