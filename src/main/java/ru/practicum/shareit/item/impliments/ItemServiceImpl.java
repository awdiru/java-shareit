package ru.practicum.shareit.item.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exceptions.IncorrectItemException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
class ItemServiceImpl implements ItemService {
    private final ItemRepository repos;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(final ItemRepository repos, final UserService userService) {
        this.repos = repos;
        this.userService = userService;
    }

    @Override
    public ItemDto createItem(final ItemDto itemDto, final long userId)
            throws IncorrectUserIdException, IncorrectItemException {

        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            log.warn("ItemServiceImpl: createItem FALSE, incorrect item name");
            throw new IncorrectItemException("Название вещи не может быть пустым");

        } else if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.warn("ItemServiceImpl: createItem FALSE, incorrect item description");
            throw new IncorrectItemException("Описание вещи не может быть пустым");

        } else if (itemDto.getAvailable() == null) {
            log.warn("ItemServiceImpl: createItem FALSE, incorrect item available");
            throw new IncorrectItemException("Статус вещи не может быть пустым");
        }

        if (userService.getUser(userId) == null) {
            log.warn("ItemServiceImpl: createItem FALSE, Incorrect user id");
            throw new IncorrectUserIdException("Пользователь с id " + userId + " не найден.");
        }

        itemDto.setOwner(userId);
        log.info("ItemServiceImpl: createItem");
        return repos.createItem(itemDto);
    }

    @Override
    public ItemDto updateItem(final long itemId, ItemDto itemDto, final long userId)
            throws IncorrectUserIdException, IncorrectItemException {

        ItemDto item = getItem(itemId);
        if (item == null) {
            log.warn("ItemServiceImpl: updateItem FALSE, Incorrect item id");
            throw new IncorrectItemException("Вещь с id " + itemId + " не найдена.");

        } else if (item.getOwner() != userId) {
            log.warn("ItemServiceImpl: updateItem FALSE. Incorrect user ID");
            throw new IncorrectUserIdException("редактировать вещь может только ее владелец.");
        }
        itemDto.setId(itemId);
        itemDto.setOwner(userId);
        log.info("ItemServiceImpl: updateItem");
        return repos.updateItem(itemDto);
    }

    @Override
    public ItemDto getItem(final long itemId) {

        log.info("ItemServiceImpl: getItem");
        return repos.getItem(itemId);
    }

    @Override
    public List<ItemDto> getItemsUser(final long userId) {

        log.info("ItemServiceImpl: getItemsUser");
        return repos.getItemsUser(userId);
    }

    @Override
    public List<ItemDto> searchItems(final String text) {

        log.info("ItemServiceImpl: searchItems");
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return repos.searchItems(text);
    }
}
