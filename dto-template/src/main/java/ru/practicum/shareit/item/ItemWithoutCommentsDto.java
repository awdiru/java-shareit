package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.request.RequestOutDto;
import ru.practicum.shareit.user.UserDto;

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
