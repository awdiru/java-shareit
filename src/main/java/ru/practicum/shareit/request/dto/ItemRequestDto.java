package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Шаблон ItemRequest на создание вещи для передачи данных
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ItemRequestDto {
    private long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
