package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentIncDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWidthBookingsTimeDto;

import java.util.List;

/**
 * Интерфейс сервиса для ItemController
 */
public interface ItemService {
    /**
     * Сохранить новую вещь.
     *
     * @param itemDto новая вещь
     * @param userId  идентификатор владельца
     * @return новая вещь
     */
    ItemDto createItem(ItemDto itemDto, long userId);

    /**
     * Редактировать существующую вещь.
     *
     * @param itemId  идентификатор вещи
     * @param itemDto новая вещь
     * @param userId  идентификатор владельца
     * @return отредактированная вещь
     */
    ItemDto updateItem(long itemId, ItemDto itemDto, long userId);

    /**
     * Вернуть вещь по идентификатору.
     *
     * @param itemId идентификатор вещи
     * @return искомая вещь
     */
    ItemWidthBookingsTimeDto getItem(long itemId, long userId);

    /**
     * Вернуть список всех вещей пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список вещей пользователя
     */
    List<ItemWidthBookingsTimeDto> getItemsUser(long userId);

    /**
     * Поиск вещи по фрагменту текста.
     *
     * @param text фрагмент текста
     * @return список искомых вещей
     */
    List<ItemDto> searchItems(String text);

    /**
     * Добавить комментарий к вещи
     *
     * @param comment комментарий
     * @param itemId  id вещи
     * @param userId  id пользователя
     * @return добавленный комментарий
     */
    CommentOutDto addComment(CommentIncDto comment, long itemId, long userId);
}
