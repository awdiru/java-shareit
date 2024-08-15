package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.ItemToRequestDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Request DTO шаблон для исходящих ответов со списком вещей
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestWithItemDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemToRequestDto> items;
}