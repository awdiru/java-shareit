package ru.practicum.shareit.item.dto.model.item;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemIncDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}