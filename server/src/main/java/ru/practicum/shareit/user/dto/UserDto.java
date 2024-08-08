package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * Шаблон User для передачи данных
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
