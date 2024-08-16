package ru.practicum.shareit.request;

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
import ru.practicum.shareit.model.dto.request.RequestIncDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.practicum.shareit.constants.Headers.USER_ID_HEADER;

/**
 * RestController для работы приложения по пути /requests
 */
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Запросы на создание вещей")
public class RequestController {
    private final RequestClient client;

    @Operation(summary = "Создать запрос",
            description = """
                    Создать новый запрос на предоставление вещи.
                    Передается id пользователя и описание запроса
                    """)
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createRequest(@Parameter(description = "id пользователя")
                                                @RequestHeader(USER_ID_HEADER) final Long userId,
                                                @RequestBody @Valid final RequestIncDto requestDto) {

        log.info("Create request; userId={}", userId);
        return client.createRequest(userId, requestDto);
    }

    @Operation(summary = "Вернуть все запросы пользователя",
            description = """
                    Вернуть все запросы пользователя со списками ответов на них.
                    Один ответ состоит из id вещи, описания и id владельца
                    """)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRequestsUser(@Parameter(description = "id пользователя")
                                                  @RequestHeader(USER_ID_HEADER) final Long userId) {

        log.info("Get user requests; userId={}", userId);
        return client.getRequestsUser(userId);
    }

    @Operation(summary = "Вернуть все запросы других пользователей",
            description = """
                    Вернуть страницу № from размера size с запросами других пользователей.
                    """)
    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllRequests(@Parameter(description = "id пользователя")
                                                 @RequestHeader(USER_ID_HEADER) final Long userId,
                                                 @Parameter(description = "№ страницы")
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") final Integer from,
                                                 @Parameter(description = "размер страницы")
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") final Integer size) {

        log.info("Get all requests; userId={}, from={}, size={}", userId, from, size);
        return client.getAllRequests(userId, from, size);
    }

    @Operation(summary = "Вернуть конкретный запрос",
            description = """
                    Вернуть запрос по его id
                    """)
    @GetMapping(value = "/{requestId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRequest(@Parameter(description = "id запроса")
                                             @PathVariable final Long requestId) {

        log.info("Get request; requestId={}", requestId);
        return client.getRequest(requestId);
    }
}
