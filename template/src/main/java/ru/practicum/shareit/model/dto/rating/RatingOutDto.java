package ru.practicum.shareit.model.dto.rating;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RatingOutDto {
    Long id;
    Long userId;
    Long itemId;
    Integer rating;
}
