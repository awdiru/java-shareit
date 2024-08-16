package ru.practicum.shareit.booking.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.practicum.shareit.model.enums.BookingStatusEnum;


/**
 * Класс для конвертации BookingStatusEnum в String
 * и наоборот для корректной работы базы данных
 */
@Converter(autoApply = true)
public class BookingStatusConverter implements AttributeConverter<BookingStatusEnum, String> {
    @Override
    public String convertToDatabaseColumn(BookingStatusEnum bookingStatusEnum) {
        return switch (bookingStatusEnum) {
            case APPROVED -> "APPROVED";
            case REJECTED -> "REJECTED";
            case CANCELED -> "CANCELED";
            default -> "WAITING";
        };
    }

    @Override
    public BookingStatusEnum convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case "APPROVED" -> BookingStatusEnum.APPROVED;
            case "REJECTED" -> BookingStatusEnum.REJECTED;
            case "CANCELED" -> BookingStatusEnum.CANCELED;
            default -> BookingStatusEnum.WAITING;
        };
    }
}