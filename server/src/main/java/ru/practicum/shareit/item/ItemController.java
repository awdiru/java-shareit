package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.annotations.ItemControllerExceptionHandler;
import ru.practicum.shareit.model.dto.comment.CommentIncDto;
import ru.practicum.shareit.model.dto.comment.CommentOutDto;
import ru.practicum.shareit.model.dto.item.ItemIncDto;
import ru.practicum.shareit.model.dto.item.ItemOutDto;
import ru.practicum.shareit.model.dto.item.ItemWidthBookingsTimeDto;

import java.util.List;

import static ru.practicum.shareit.constants.Headers.USER_ID_HEADER;

/**
 * RestController для работы приложения по пути /items
 */
@RestController
@Slf4j
@RequestMapping("/items")
@ItemControllerExceptionHandler
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    /**
     * Запрос на создание вещи
     *
     * @param itemDto входящий запрос на создание вещи
     * @param userId  id пользователя
     * @return созданная вещь
     */
    @PostMapping
    public ItemOutDto createItem(@RequestBody final ItemIncDto itemDto,
                                 @RequestHeader(USER_ID_HEADER) final Long userId) {

        log.info("Create Item; userId={} ", userId);
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
                                 @RequestHeader(USER_ID_HEADER) final Long userId) {

        log.info("Update Item; userId={}, itemId={} ", userId, itemId);
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
    public ItemWidthBookingsTimeDto getItem(@RequestHeader(USER_ID_HEADER) final Long userId,
                                            @PathVariable final Long itemId) {

        log.info("Get Item; userId={}, itemId={} ", userId, itemId);
        return itemService.getItem(itemId, userId);
    }

    /**
     * Посмотреть все вещи пользователя
     *
     * @param userId id пользователя
     * @return список вещей пользователя
     */
    @GetMapping
    public List<ItemWidthBookingsTimeDto> getItemsUser(@RequestHeader(USER_ID_HEADER) final Long userId,
                                                       @RequestParam final Integer from,
                                                       @RequestParam final Integer size) {

        log.info("Get Items user; userId={}, from={}, size={}", userId, from, size);
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

        log.info("Search Items; text={}", text);
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
    public CommentOutDto addComment(@RequestHeader(USER_ID_HEADER) final Long userId,
                                    @RequestBody final CommentIncDto comment,
                                    @PathVariable final Long itemId) {

        log.info("Add Comment; userId={}, itemId={}", userId, itemId);
        return itemService.addComment(comment, itemId, userId);
    }
}
