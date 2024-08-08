package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.annotations.ItemControllerExceptionHandler;
import ru.practicum.shareit.item.dto.model.*;

import java.util.List;

/**
 * RestController для работы приложения по пути /items
 */
@RestController
@Slf4j
@RequestMapping("/items")
@ItemControllerExceptionHandler
public class ItemController {
    private final ItemService itemService;
    private final String USER_ID_HEAD = "X-Sharer-User-Id";

    @Autowired
    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Запрос на создание вещи
     *
     * @param itemDto входящий запрос на создание вещи
     * @param userId  id пользователя
     * @return созданная вещь
     */
    @PostMapping
    public ItemOutDto createItem(@RequestBody final ItemIncDto itemDto,
                                 @RequestHeader(USER_ID_HEAD) final Long userId) {

        log.info("ItemController: createItem");
        return itemService.createItem(itemDto, userId);
    }

    /**
     * Обновление вещи
     *
     * @param itemId  id вещи
     * @param itemDto входящий запрос для обновления вещи
     * @param userId  id пользователя
     * @return обновленная вещь
     */
    @PatchMapping("/{itemId}")
    public ItemOutDto updateItem(@PathVariable final Long itemId,
                                 @RequestBody final ItemIncDto itemDto,
                                 @RequestHeader(USER_ID_HEAD) final Long userId) {

        log.info("ItemController: updateItem");
        return itemService.updateItem(itemId, itemDto, userId);
    }

    /**
     * Посмотреть информацию о вещи
     *
     * @param userId id пользователя
     * @param itemId id вещи
     * @return информация о вещи
     */
    @GetMapping("/{itemId}")
    public ItemWidthBookingsTimeDto getItem(@RequestHeader(USER_ID_HEAD) final Long userId,
                                            @PathVariable final Long itemId) {

        log.info("ItemController: getItem");
        return itemService.getItem(itemId, userId);
    }

    /**
     * Посмотреть все вещи пользователя
     *
     * @param userId id пользователя
     * @return список вещей пользователя
     */
    @GetMapping
    public List<ItemWidthBookingsTimeDto> getItemsUser(@RequestHeader(USER_ID_HEAD) final Long userId,
                                                       @RequestParam final Integer from,
                                                       @RequestParam final Integer size) {

        log.info("ItemController: getItemsUser");
        return itemService.getItemsUser(userId, from, size);
    }

    /**
     * Найти вещь по совпадению с текстом запроса
     *
     * @param text текст запроса
     * @return найденные вещи
     */
    @GetMapping("/search")
    public List<ItemOutDto> searchItems(@RequestParam final String text) {

        log.info("ItemController: searchItems");
        return itemService.searchItems(text);
    }

    /**
     * Добавить комментарий к вещи
     *
     * @param userId  id пользователя
     * @param comment комментарий
     * @param itemId  id вещи
     * @return добавленный комментарий
     */
    @PostMapping("/{itemId}/comment")
    public CommentOutDto addComment(@RequestHeader(USER_ID_HEAD) final Long userId,
                                    @RequestBody final CommentIncDto comment,
                                    @PathVariable final Long itemId) {

        log.info("ItemController: addComment");
        return itemService.addComment(comment, itemId, userId);
    }
}
