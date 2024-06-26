package ru.practicum.shareit.item.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> repos;
    private long idCount = 0;

    public ItemRepositoryImpl() {
        this.repos = new HashMap<>();
    }

    @Override
    public ItemDto createItem(ItemDto ItemDto) {

        Item item = ItemMapper.toItem(ItemDto);
        item.setId(++idCount);
        repos.put(idCount, item);
        log.info("ItemRepositoryImpl: saveItem, idItem: " + idCount);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto ItemDto) {

        Item item = repos.get(ItemDto.getId());
        Item updItem = ItemMapper.toItem(ItemDto);

        if (updItem.getName() != null)
            item.setName(updItem.getName());
        if (updItem.getDescription() != null)
            item.setDescription(updItem.getDescription());
        if (updItem.getAvailable() != null)
            item.setAvailable(updItem.getAvailable());

        log.info("ItemRepositoryImpl: editItem, idItem: " + ItemDto.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItem(long itemId) {

        log.info("ItemRepositoryImpl: getItem, idItem: " + itemId);
        return ItemMapper.toItemDto(repos.get(itemId));
    }

    @Override
    public List<ItemDto> getItemsUser(long userId) {

        log.info("ItemRepositoryImpl: getItemsUser, idUser: " + userId);
        return repos.values().stream()
                .filter(item -> item.getOwner() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {

        log.info("ItemRepositoryImpl: searchItems, text: " + text);
        return repos.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
