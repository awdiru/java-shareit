package ru.practicum.shareit.exception;

/**
 * Исключение при возникновении ошибки в базе данных
 */
public class DataException extends RuntimeException {
    public DataException(final String message) {
        super(message);
    }

}