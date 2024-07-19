package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Long owner;
    private Integer numberOfRentals;
    private Boolean available;
    private String request;
}
