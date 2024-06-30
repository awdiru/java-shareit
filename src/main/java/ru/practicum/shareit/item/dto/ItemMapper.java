package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(final Item item) {

        if (item == null) {
            return null;
        }

        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner(),
                item.getNumberOfRentals(),
                item.getAvailable(),
                item.getRequest());
    }

    public static Item toItem(final ItemDto itemDto) {

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
}