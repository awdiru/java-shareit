package ru.practicum.shareit.model.dto.item;

import lombok.*;
import ru.practicum.shareit.model.dto.comment.CommentOutDto;
import ru.practicum.shareit.model.dto.request.RequestOutDto;
import ru.practicum.shareit.model.dto.user.UserDto;

import java.util.List;

/**
 * Item DTO шаблон для исходящих ответов
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemOutDto {
    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private Integer numberOfRentals;
    private Boolean available;
    private RequestOutDto request;
    private Double rating;
    private Integer userRating;
    private List<CommentOutDto> comments;
}