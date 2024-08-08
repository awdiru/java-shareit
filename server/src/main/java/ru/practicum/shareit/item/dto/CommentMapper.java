package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

/**
 * Конвертер Comment классов
 */
@Component
public class CommentMapper {
    public Comment toCommentFromCommentIncDto(CommentIncDto commentDto) {
        return new Comment(null, null, null,
                commentDto.getText(), null);
    }

    public CommentOutDto toCommentOutDtoFromComment(Comment comment) {
        return new CommentOutDto(comment.getId(),
                comment.getAuthor().getName(),
                comment.getText(),
                comment.getCreated());
    }
}
