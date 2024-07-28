package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.IncorrectCommentatorException;
import ru.practicum.shareit.exceptions.IncorrectItemIdException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
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
     * @throws IncorrectUserIdException некорректный id пользователя
     */
    @PostMapping
    public ItemDto createItem(@RequestBody @Valid final ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") @NotBlank final long userId)
            throws IncorrectUserIdException {

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
     * @throws IncorrectUserIdException некорректный id пользователя
     * @throws IncorrectItemIdException некорректный id вещи
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable final long itemId,
                              @RequestBody final ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") final long userId)
            throws IncorrectUserIdException, IncorrectItemIdException {

        log.info("ItemController: updateItem");
        return itemService.updateItem(itemId, itemDto, userId);

    }

    /**
     * Посмотреть информацию о вещи
     *
     * @param userId id пользователя
     * @param itemId id вещи
     * @return информация о вещи
     * @throws IncorrectItemIdException некорректный id вещи
     */
    @GetMapping("/{itemId}")
    public ItemWidthBookingsTimeDto getItem(@RequestHeader("X-Sharer-User-Id") final long userId,
                                            @PathVariable final long itemId)
            throws IncorrectItemIdException {

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
     * @throws IncorrectCommentatorException исключения при попытке написать комментарий к вещи,
     *                                       которую пользователь не брал
     */
    @PostMapping("/{itemId}/comment")
    public CommentOutDto addComment(@RequestHeader("X-Sharer-User-Id") final long userId,
                                    @RequestBody @Valid final CommentIncDto comment,
                                    @PathVariable final long itemId) throws IncorrectCommentatorException {

        return itemService.addComment(comment, itemId, userId);
    }
}
