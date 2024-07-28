package ru.practicum.shareit.booking.converter;

import ru.practicum.shareit.booking.enums.BookingStatusEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Класс для конвертации BookingStatusEnum в String
 * и наоборот для корректной работы базы данных
 */
@Converter(autoApply = true)
public class BookingStatusConverter implements AttributeConverter<BookingStatusEnum, String> {
    @Override
    public String convertToDatabaseColumn(BookingStatusEnum bookingStatusEnum) {
        switch (bookingStatusEnum) {
            case APPROVED:
                return "APPROVED";
            case REJECTED:
                return "REJECTED";
            case CANCELED:
                return "CANCELED";
            default:
                return "WAITING";
        }
    }

    @Override
    public BookingStatusEnum convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "APPROVED":
                return BookingStatusEnum.APPROVED;
            case "REJECTED":
                return BookingStatusEnum.REJECTED;
            case "CANCELED":
                return BookingStatusEnum.CANCELED;
            default:
                return BookingStatusEnum.WAITING;
        }
    }
}
