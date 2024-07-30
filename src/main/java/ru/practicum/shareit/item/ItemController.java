package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.annotations.ItemControllerExceptionHandler;
import ru.practicum.shareit.item.dto.CommentIncDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWidthBookingsTimeDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    public ItemDto createItem(@RequestBody @Valid final ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") @NotBlank final long userId) {

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
    public ItemDto updateItem(@PathVariable final long itemId,
                              @RequestBody final ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") final long userId) {

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
    public ItemWidthBookingsTimeDto getItem(@RequestHeader("X-Sharer-User-Id") final long userId,
                                            @PathVariable final long itemId) {

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
    public List<ItemWidthBookingsTimeDto> getItemsUser(@RequestHeader("X-Sharer-User-Id") final long userId) {

        log.info("ItemController: getItemsUser");
        return itemService.getItemsUser(userId);
    }

    /**
     * Найти вещь по совпадению с текстом запроса
     *
     * @param text текст запроса
     * @return найденные вещи
     */
    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {

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
    public CommentOutDto addComment(@RequestHeader("X-Sharer-User-Id") final long userId,
                                    @RequestBody @Valid final CommentIncDto comment,
                                    @PathVariable final long itemId) {

        log.info("ItemController: addComment");
        return itemService.addComment(comment, itemId, userId);
    }
}
