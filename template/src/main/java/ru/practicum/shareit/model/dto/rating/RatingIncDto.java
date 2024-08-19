package ru.practicum.shareit.model.dto.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(description = "Сущность оценки")
public class RatingIncDto {
    @Min(1) @Max(10)
    @NotNull
    @Schema(description = "оценка")
    Integer rating;
}
