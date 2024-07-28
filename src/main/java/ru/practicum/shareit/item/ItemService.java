package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.IncorrectCommentatorException;
import ru.practicum.shareit.exceptions.IncorrectItemIdException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
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
    ItemDto createItem(ItemDto itemDto, long userId) throws IncorrectUserIdException;

    /**
     * Редактировать существующую вещь.
     *
     * @param itemId  идентификатор вещи
     * @param itemDto новая вещь
     * @param userId  идентификатор владельца
     * @return отредактированная вещь
     */
    ItemDto updateItem(long itemId, ItemDto itemDto, long userId) throws IncorrectUserIdException, IncorrectItemIdException;

    /**
     * Вернуть вещь по идентификатору.
     *
     * @param itemId идентификатор вещи
     * @return искомая вещь
     */
    ItemWidthBookingsTimeDto getItem(long itemId, Long userId) throws IncorrectItemIdException;

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

    CommentOutDto addComment(CommentIncDto comment, Long itemId, Long userId) throws IncorrectCommentatorException;
}
