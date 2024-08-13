package ru.practicum.shareit.item.dto.model.item;

import lombok.*;
import ru.practicum.shareit.item.dto.model.comment.CommentOutDto;
import ru.practicum.shareit.request.dto.model.RequestOutDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Item шаблон для передачи данных
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class ItemOutDto {
    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private Integer numberOfRentals;
    private Boolean available;
    private RequestOutDto request;
    private List<CommentOutDto> comments;
}