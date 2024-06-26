package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    /**
     * Сохранить новую вещь
     *
     * @param ItemDto новая вещь
     * @return новая вещь
     */
    ItemDto createItem(ItemDto ItemDto);

    /**
     * Редактировать существующую вещь
     *
     * @param ItemDto новая вещь
     * @return отредактированная вещь
     */
    ItemDto updateItem(ItemDto ItemDto);

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