package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.practicum.shareit.constants.Headers.USER_ID_HEADER;

/**
 * RestController для работы приложения по пути /items
 */
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Вещи")
public class ItemController {
    private final ItemClient client;

    @Operation(summary = "Создать новую вещь",
            description = """
                    Создать новую вещь.
                    Обязательны название, описание и доступность вещи для аренды.
                    Опционально - id запроса, о которому создана вещь
                    """)
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createItem(@Parameter(description = "id пользователя")
                                             @RequestHeader(USER_ID_HEADER) final Long userId,
                                             @RequestBody @Valid final ItemIncDto itemDto
    ) {

        log.info("POST create Item; userId={} ", userId);
        return client.createItem(itemDto, userId);
    }

    @Operation(summary = "Обновить вещь",
            description = """
                    Обновить существующую вещь.
                    Сделать может только владелец вещи.
                    """)
    @PatchMapping(value = "/{itemId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateItem(@Parameter(description = "id пользователя")
                                             @RequestHeader(USER_ID_HEADER) final Long userId,
                                             @Parameter(description = "id вещи")
                                             @PathVariable final Long itemId,
                                             @RequestBody final ItemIncDto itemDto
    ) {

        log.info("PATCH update Item; userId={}, itemId={} ", userId, itemId);
        return client.updateItem(itemId, itemDto, userId);
    }

    @Operation(summary = "Вернуть информацию о вещи",
            description = """
                    Вернуть информацию о конкретной вещи
                    Владелец вещи так же увидит данные о последнем и ближайшем бронировании
                    """)
    @GetMapping(value = "/{itemId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getItem(@Parameter(description = "id пользователя")
                                          @RequestHeader(USER_ID_HEADER) final Long userId,
                                          @Parameter(description = "id вещи")
                                          @PathVariable final Long itemId) {

        log.info("GET Item; userId={}, itemId={} ", userId, itemId);
        return client.getItem(userId, itemId);
    }

    @Operation(summary = "Вернуть все вещи пользователя",
            description = """
                    Вернуть страницу № from размера size с вещами пользователя.
                    """)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getItemsUser(@Parameter(description = "id пользователя")
                                               @RequestHeader(USER_ID_HEADER) final Long userId,
                                               @Parameter(description = "№ страницы")
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") final Integer from,
                                               @Parameter(description = "Размер страницы")
                                               @Positive @RequestParam(name = "size", defaultValue = "10") final Integer size) {

        log.info("GET Items user; userId={}, from={}, size={}", userId, from, size);
        return client.getItemsUser(userId, from, size);
    }

    @Operation(summary = "Найти вещь по запросу",
            description = """
                    Найти вещи по совпадению в имени или описании.
                    Регистр букв не важен.
                    Возвращает список вещей.
                    Если вещь не будет найдена, вернется пустой список.
                    """)
    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchItems(@Parameter(description = "текст запроса")
                                              @RequestParam final String text) {

        log.info("GET search Items; text={}", text);
        return client.searchItems(text);
    }

    @Operation(summary = "Добавить комментарий к вещи",
            description = """
                    Добавить комментарий к вещи.
                    Разрешено только тем пользователям, которые брали вещь в аренду.
                    """)
    @PostMapping(value = "/{itemId}/comment", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addComment(@Parameter(description = "id пользователя")
                                             @RequestHeader(USER_ID_HEADER) final Long userId,
                                             @Parameter (description = "id вещи")
                                             @PathVariable final Long itemId,
                                             @RequestBody @Valid final CommentIncDto comment) {

        log.info("POST add Comment; userId={}, itemId={}", userId, itemId);
        return client.addComment(userId, comment, itemId);
    }
}