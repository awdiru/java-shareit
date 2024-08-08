package ru.practicum.shareit.item.dto.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.dto.model.RequestOutDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Item шаблон для передачи данных
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ItemOutDto {
    private Long id;
    private String name;
    private String description;
    private User owner;
    private Integer numberOfRentals;
    private Boolean available;
    private RequestOutDto request;
    private List<CommentOutDto> comments;
}