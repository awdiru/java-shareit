package ru.practicum.shareit.item.impliments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.IncorrectItemException;
import ru.practicum.shareit.exceptions.IncorrectItemIdException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            throws IncorrectUserIdException {

        if (userService.getUser(userId) == null) {
            log.warn("ItemServiceImpl: createItem FALSE, Incorrect user id");
            throw new IncorrectUserIdException("Пользователь с id " + userId + " не найден.");
        }
        User user = UserMapper.toUser(userService.getUser(userId));
        itemDto.setOwner(user);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(repos.save(item));
    }

    @Override
    public ItemDto updateItem(final long itemId, ItemDto itemDto, final long userId)
            throws IncorrectUserIdException, IncorrectItemException {

        Item item = repos.findById(itemId);
        if (item == null) {
            log.warn("ItemServiceImpl: updateItem FALSE, Incorrect item id");
            throw new IncorrectItemException("Вещь с id " + itemId + " не найдена.");

        } else if (item.getOwner().getId() != userId) {
            log.warn("ItemServiceImpl: updateItem FALSE. Incorrect user ID");
            throw new IncorrectUserIdException("редактировать вещь может только ее владелец.");
        }

        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getNumberOfRentals() != null)
            item.setNumberOfRentals(itemDto.getNumberOfRentals());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        item.setOwner(item.getOwner());
        return ItemMapper.toItemDto(repos.save(item));
    }

    @Override
    public ItemDto getItem(final long itemId) throws IncorrectItemIdException {
        log.info("ItemServiceImpl: getItem");
        Item item = repos.findById(itemId);
        if(item == null)
            throw new IncorrectItemIdException("Вещь с id " + itemId + " не найдена");
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsUser(final long userId) {
        log.info("ItemServiceImpl: getItemsUser");
        return repos.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(final String text) {
        log.info("ItemServiceImpl: searchItems");
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return repos.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
