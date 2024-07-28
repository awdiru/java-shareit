package ru.practicum.shareit.item.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemRepositoryOld;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Реализация репозитория для хранения информации об Item классах
 * в оперативной памяти
 * Устаревший, не используется
 */
@Component
@Slf4j
class ItemRepositoryOldImpl implements ItemRepositoryOld {
    private final Map<Long, Item> repos;
    private long idCount = 0;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemRepositoryOldImpl(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
        this.repos = new HashMap<>();
    }

    @Override
    public ItemDto createItem(final ItemDto itemDto) {

        Item item = itemMapper.toItemFromItemDto(itemDto);
        item.setId(++idCount);
        repos.put(idCount, item);
        log.info("ItemRepositoryImpl: saveItem, idItem: " + idCount);
        return itemMapper.toItemDtoFromItem(item);
    }

    @Override
    public ItemDto updateItem(final ItemDto itemDto) {

        Item item = repos.get(itemDto.getId());
        Item updItem = itemMapper.toItemFromItemDto(itemDto);

        if (updItem.getName() != null) {
            item.setName(updItem.getName());
        }
        if (updItem.getDescription() != null) {
            item.setDescription(updItem.getDescription());
        }
        if (updItem.getAvailable() != null) {
            item.setAvailable(updItem.getAvailable());
        }
        log.info("ItemRepositoryImpl: editItem, idItem: " + itemDto.getId());
        return itemMapper.toItemDtoFromItem(item);
    }

    @Override
    public ItemDto getItem(final long itemId) {

        log.info("ItemRepositoryImpl: getItem, idItem: " + itemId);
        return itemMapper.toItemDtoFromItem(repos.get(itemId));
    }

    @Override
    public List<ItemDto> getItemsUser(final long userId) {

        log.info("ItemRepositoryImpl: getItemsUser, idUser: " + userId);
        return repos.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(itemMapper::toItemDtoFromItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(final String text) {

        log.info("ItemRepositoryImpl: searchItems, text: " + text);
        return repos.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .map(itemMapper::toItemDtoFromItem)
                .collect(Collectors.toList());
    }
}
