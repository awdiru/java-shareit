package ru.practicum.shareit.exceptions;

public class IncorrectUserIdException extends Exception {
    public IncorrectUserIdException(final String message) {
        super(message);
    }
}
