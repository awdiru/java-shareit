package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Конвертер Item классов
 */
@Component
public class ItemMapper {
    public ItemDto toItemDtoFromItem(final Item item) {
        if (item == null) {
            return null;
        }

        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner(),
                item.getNumberOfRentals(),
                item.getAvailable(),
                item.getRequest(),
                null);
    }

    public Item toItemFromItemDto(final ItemDto itemDto) {

        if (itemDto == null) {
            return null;
        }

        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getOwner(),
                itemDto.getNumberOfRentals(),
                itemDto.getAvailable(),
                itemDto.getRequest());
    }

    public ItemWidthBookingsTimeDto toItemWidthBookingsTimeDtoFromItem(Item item) {
        return new ItemWidthBookingsTimeDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner(),
                item.getNumberOfRentals(),
                item.getAvailable(),
                item.getRequest(),
                null,
                null,
                null);
    }
}