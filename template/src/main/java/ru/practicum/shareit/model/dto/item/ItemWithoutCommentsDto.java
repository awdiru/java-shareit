package ru.practicum.shareit.model.dto.item;

import lombok.*;
import ru.practicum.shareit.model.dto.request.RequestOutDto;
import ru.practicum.shareit.model.dto.user.UserDto;

/**
 * Item DTO шаблон для нотификации пользователя по email
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemWithoutCommentsDto {
    private String name;
    private String description;
    private UserDto owner;
    private Boolean available;
    private RequestOutDto request;
}
