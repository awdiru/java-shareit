package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.RequestParam;
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
    ItemDto createItem(ItemDto itemDto, Long userId);

    /**
     * Редактировать существующую вещь.
     *
     * @param itemId  идентификатор вещи
     * @param itemDto новая вещь
     * @param userId  идентификатор владельца
     * @return отредактированная вещь
     */
    ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId);

    /**
     * Вернуть вещь по идентификатору.
     *
     * @param itemId идентификатор вещи
     * @return искомая вещь
     */
    ItemWidthBookingsTimeDto getItem(Long itemId, Long userId);

    /**
     * Вернуть список всех вещей пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список вещей пользователя
     */
    List<ItemWidthBookingsTimeDto> getItemsUser(Long userId,
                                                Integer from,
                                                Integer size);

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
    CommentOutDto addComment(CommentIncDto comment, Long itemId, Long userId);
}
