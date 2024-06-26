package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
}

