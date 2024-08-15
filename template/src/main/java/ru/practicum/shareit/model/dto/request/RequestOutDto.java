package ru.practicum.shareit.model.dto.request;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Request DTO шаблон для исходящих ответов
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestOutDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}