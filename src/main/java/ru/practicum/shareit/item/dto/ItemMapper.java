package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {

        if (item == null)
            return null;

        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner(),
                item.getNumberOfRentals(),
                item.getAvailable(),
                item.getRequest());
    }

    public static Item toItem(ItemDto ItemDto) {

        if (ItemDto == null)
            return null;

        return new Item(ItemDto.getId(),
                ItemDto.getName(),
                ItemDto.getDescription(),
                ItemDto.getOwner(),
                ItemDto.getNumberOfRentals(),
                ItemDto.getAvailable(),
                ItemDto.getRequest());
    }
}
