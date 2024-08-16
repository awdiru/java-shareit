package ru.practicum.shareit.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Сущность пользователя")
public class UserDto {
    @Schema(description = "идентификатор")
    private Long id;
    @Schema(description = "Имя пользователя", defaultValue = "Иванов Иван Иванович")
    private String name;
    @Email
    @Schema(description = "Email пользователя",defaultValue = "iVan.1999@email.com")
    private String email;
}