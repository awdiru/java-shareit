package ru.practicum.shareit.item.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemIncDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}