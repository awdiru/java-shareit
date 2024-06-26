package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ItemRequest {
    private long id;
    private String description;
    private long requestor;
    private LocalDateTime created;
}

