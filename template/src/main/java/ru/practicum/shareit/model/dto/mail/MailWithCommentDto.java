package ru.practicum.shareit.model.dto.mail;

import lombok.*;
import ru.practicum.shareit.model.dto.comment.CommentOutDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class MailWithCommentDto {
    private String to;
    private CommentOutDto comment;
}
