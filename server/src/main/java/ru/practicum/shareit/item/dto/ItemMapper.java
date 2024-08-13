package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.model.ItemIncDto;
import ru.practicum.shareit.item.dto.model.ItemOutDto;
import ru.practicum.shareit.item.dto.model.ItemToRequestDto;
import ru.practicum.shareit.item.dto.model.ItemWidthBookingsTimeDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.dto.UserMapper;

/**
 * Конвертер Item классов
 */
@Component
public class ItemMapper {
    private final RequestMapper requestMapper = new RequestMapper();
    private final UserMapper userMapper = new UserMapper();

    public ItemOutDto toItemDtoFromItem(final Item item) {
        if (item == null) return null;
        ItemOutDto itemOutDto = new ItemOutDto(item.getId(),
                item.getName(),
                item.getDescription(),
                userMapper.toUserDto(item.getOwner()),
                item.getNumberOfRentals(),
                item.getAvailable(),
                null,
                null);

        if (item.getRequest() != null)
            itemOutDto.setRequest(requestMapper.toRequestOutDtoFromRequest(item.getRequest()));

        return itemOutDto;
    }

    public ItemWidthBookingsTimeDto toItemWidthBookingsTimeDtoFromItem(Item item) {
        return new ItemWidthBookingsTimeDto(item.getId(),
                item.getName(),
                item.getDescription(),
                userMapper.toUserDto(item.getOwner()),
                item.getNumberOfRentals(),
                item.getAvailable(),
                requestMapper.toRequestOutDtoFromRequest(item.getRequest()),
                null,
                null,
                null);
    }

    public ItemToRequestDto toItemToRequestFromItem(Item item) {
        return new ItemToRequestDto(item.getId(),
                item.getName(),
                item.getOwner().getId());
    }

    public Item toItemFromItemIncDto(ItemIncDto item) {
        return new Item(null,
                item.getName(),
                item.getDescription(),
                null,
                null,
                item.getAvailable(),
                null);
    }
}