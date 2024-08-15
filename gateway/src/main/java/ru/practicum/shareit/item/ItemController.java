package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.model.dto.comment.CommentIncDto;
import ru.practicum.shareit.model.dto.item.ItemIncDto;

import static ru.practicum.shareit.constants.Headers.USER_ID_HEADER;

/**
 * RestController для работы приложения по пути /items
 */
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient client;

    /**
     * Запрос на создание вещи
     *
     * @param itemDto входящий запрос на создание вещи
     * @param userId  id пользователя
     * @return ответ сервера
     */
    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody @Valid final ItemIncDto itemDto,
                                             @RequestHeader(USER_ID_HEADER) final Long userId) {

        log.info("POST create Item; userId={} ", userId);
        return client.createItem(itemDto, userId);
    }

    /**
     * Обновление вещи
     *
     * @param itemId  id вещи
     * @param itemDto входящий запрос для обновления вещи
     * @param userId  id пользователя
     * @return ответ сервера
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable final Long itemId,
                                             @RequestBody final ItemIncDto itemDto,
                                             @RequestHeader(USER_ID_HEADER) final Long userId) {

        log.info("PATCH update Item; userId={}, itemId={} ", userId, itemId);
        return client.updateItem(itemId, itemDto, userId);
    }

    /**
     * Посмотреть информацию о вещи
     *
     * @param userId id пользователя
     * @param itemId id вещи
     * @return ответ сервера
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(USER_ID_HEADER) final Long userId,
                                          @PathVariable final Long itemId) {

        log.info("GET Item; userId={}, itemId={} ", userId, itemId);
        return client.getItem(userId, itemId);
    }

    /**
     * Посмотреть все вещи пользователя
     *
     * @param userId id пользователя
     * @param from   индекс страницы
     * @param size   размер страницы
     * @return ответ сервера
     */
    @GetMapping
    public ResponseEntity<Object> getItemsUser(@RequestHeader(USER_ID_HEADER) final Long userId,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") final Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") final Integer size) {

        log.info("GET Items user; userId={}, from={}, size={}", userId, from, size);
        return client.getItemsUser(userId, from, size);
    }

    /**
     * Найти вещь по совпадению с текстом запроса
     *
     * @param text текст запроса
     * @return ответ сервера
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam final String text) {

        log.info("GET search Items; text={}", text);
        return client.searchItems(text);
    }

    /**
     * Добавить комментарий к вещи
     *
     * @param userId  id пользователя
     * @param comment комментарий
     * @param itemId  id вещи
     * @return ответ сервера
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) final Long userId,
                                             @RequestBody @Valid final CommentIncDto comment,
                                             @PathVariable final Long itemId) {

        log.info("POST add Comment; userId={}, itemId={}", userId, itemId);
        return client.addComment(userId, comment, itemId);
    }
}