package ru.practicum.shareit.exception;

/**
 * Исключение при попытке добавить комментарий пользователем, который не арендовал вещь
 */
public class IncorrectCommentatorException extends RuntimeException {
    public IncorrectCommentatorException(final String message) {
        super(message);
    }
}