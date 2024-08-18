package ru.practicum.shareit.model.dto.mail;

import lombok.*;
import ru.practicum.shareit.model.dto.item.ItemWithoutCommentsDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class MailWithItemDto {
    private String to;
    private ItemWithoutCommentsDto item;
}
