package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.dto.item.ItemIncDto;
import ru.practicum.shareit.model.dto.item.ItemOutDto;
import ru.practicum.shareit.model.dto.item.ItemWidthBookingsTimeDto;
import ru.practicum.shareit.model.dto.item.ItemWithoutCommentsDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.dto.UserMapper;

/**
 * Конвертер Item классов
 */
@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final RequestMapper requestMapper;
    private final UserMapper userMapper;

    public ItemOutDto toItemDtoFromItem(final Item item) {
        if (item == null) return null;
        ItemOutDto itemOutDto = new ItemOutDto(item.getId(),
                item.getName(),
                item.getDescription(),
                userMapper.toUserDto(item.getOwner()),
                item.getNumberOfRentals(),
                item.getAvailable(),
                null, null, null, null);

        if (item.getRequest() != null)
            itemOutDto.setRequest(requestMapper.toRequestOutDtoFromRequest(item.getRequest()));

        return itemOutDto;
    }

    public ItemWidthBookingsTimeDto toItemWidthBookingsTimeDtoFromItem(Item item) {
        return item == null ? null :
                new ItemWidthBookingsTimeDto(item.getId(),
                        item.getName(),
                        item.getDescription(),
                        userMapper.toUserDto(item.getOwner()),
                        item.getNumberOfRentals(),
                        item.getAvailable(),
                        requestMapper.toRequestOutDtoFromRequest(item.getRequest()),
                        null, null, null, null, null);
    }

    public Item toItemFromItemIncDto(ItemIncDto item) {
        return item == null ? null :
                new Item(null,
                        item.getName(),
                        item.getDescription(),
                        null,
                        null,
                        item.getAvailable(),
                        null);
    }

    public ItemWithoutCommentsDto toItemWithoutCommentsDtoFromItem(Item item) {
        return item == null ? null :
                new ItemWithoutCommentsDto(item.getName(),
                        item.getDescription(),
                        userMapper.toUserDto(item.getOwner()),
                        item.getAvailable(),
                        requestMapper.toRequestOutDtoFromRequest(item.getRequest()));
    }
}