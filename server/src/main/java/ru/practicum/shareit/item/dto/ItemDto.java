package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Item шаблон для передачи данных
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private User owner;
    private Integer numberOfRentals;
    @NotNull
    private Boolean available;
    private ItemRequest request;
    private List<CommentOutDto> comments;
}