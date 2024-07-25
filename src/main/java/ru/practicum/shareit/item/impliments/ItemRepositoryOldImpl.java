package ru.practicum.shareit.item.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemRepositoryOld;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
class ItemRepositoryOldImpl implements ItemRepositoryOld {
    private final Map<Long, Item> repos;
    private long idCount = 0;

    public ItemRepositoryOldImpl() {
        this.repos = new HashMap<>();
    }

    @Override
    public ItemDto createItem(final ItemDto itemDto) {

        Item item = ItemMapper.toItem(itemDto);
        item.setId(++idCount);
        repos.put(idCount, item);
        log.info("ItemRepositoryImpl: saveItem, idItem: " + idCount);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(final ItemDto itemDto) {

        Item item = repos.get(itemDto.getId());
        Item updItem = ItemMapper.toItem(itemDto);

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
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItem(final long itemId) {

        log.info("ItemRepositoryImpl: getItem, idItem: " + itemId);
        return ItemMapper.toItemDto(repos.get(itemId));
    }

    @Override
    public List<ItemDto> getItemsUser(final long userId) {

        log.info("ItemRepositoryImpl: getItemsUser, idUser: " + userId);
        return repos.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(final String text) {

        log.info("ItemRepositoryImpl: searchItems, text: " + text);
        return repos.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
