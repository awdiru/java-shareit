package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exceptions.IncorrectItemException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;

import java.util.List;

public interface ItemService {
    /**
     * Сохранить новую вещь
     *
     * @param ItemDto новая вещь
     * @param userId  идентификатор владельца
     * @return новая вещь
     */
    ItemDto createItem(ItemDto ItemDto, long userId) throws IncorrectUserIdException, IncorrectItemException;

    /**
     * Редактировать существующую вещь
     *
     * @param itemId  идентификатор вещи
     * @param ItemDto новая вещь
     * @param userId  идентификатор владельца
     * @return отредактированная вещь
     */
    ItemDto updateItem(long itemId, ItemDto ItemDto, long userId) throws IncorrectUserIdException, IncorrectItemException;

    /**
     * Вернуть вещь по идентификатору
     *
     * @param itemId идентификатор вещи
     * @return искомая вещь
     */
    ItemDto getItem(long itemId);

    /**
     * Вернуть список всех вещей пользователя
     *
     * @param userId идентификатор пользователя
     * @return список вещей пользователя
     */
    List<ItemDto> getItemsUser(long userId);

    /**
     * Поиск вещи по фрагменту текста
     *
     * @param text фрагмент текста
     * @return список искомых вещей
     */
    List<ItemDto> searchItems(String text);
}
