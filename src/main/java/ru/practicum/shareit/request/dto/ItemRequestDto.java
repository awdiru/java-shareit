package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ItemRequestDto {
    private long id;
    private String description;
    private long requestor;
    private LocalDateTime created;
}
