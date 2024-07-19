package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Long owner;
    private Integer numberOfRentals;
    private Boolean available;
    private String request;
}

