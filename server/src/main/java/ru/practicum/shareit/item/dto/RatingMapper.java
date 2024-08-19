package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Rating;
import ru.practicum.shareit.model.dto.rating.RatingIncDto;
import ru.practicum.shareit.model.dto.rating.RatingOutDto;

@Component
public class RatingMapper {
    public Rating toRatingFromRatingIncDto(RatingIncDto rating) {
        return rating == null ? null :
                new Rating(null,
                        null,
                        null,
                        rating.getRating());
    }

    public RatingOutDto toRatingOutDtoFromRating(Rating rating) {
        return rating == null ? null :
                new RatingOutDto(rating.getId(),
                        rating.getAuthor(),
                        rating.getItem(),
                        rating.getRating());
    }
}