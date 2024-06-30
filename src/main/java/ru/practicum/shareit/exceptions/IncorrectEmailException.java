package ru.practicum.shareit.exceptions;

public class IncorrectEmailException extends Exception {
    public IncorrectEmailException(final String message) {
        super(message);
    }
}
