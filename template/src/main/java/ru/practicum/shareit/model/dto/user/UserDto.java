package ru.practicum.shareit.model.dto.user;

import jakarta.validation.constraints.Email;
import lombok.*;


/**
 * User DTO шаблон для передачи данных
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDto {
    private Long id;
    private String name;
    @Email
    private String email;
}