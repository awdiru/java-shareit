package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.annotations.RequestControllerExceptionHandler;
import ru.practicum.shareit.request.dto.model.RequestIncDto;
import ru.practicum.shareit.request.dto.model.RequestOutDto;
import ru.practicum.shareit.request.dto.model.RequestWithItemDto;

import java.util.List;

/**
 * RestController для работы приложения по пути /requests
 */
@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequestControllerExceptionHandler
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final String userIdHead = "X-Sharer-User-Id";

    /**
     * Создать запрос на вещь
     *
     * @param userId        id пользователя
     * @param requestIncDto запрос на вещь
     * @return ответ сервера
     */
    @PostMapping
    public RequestOutDto createRequest(@RequestHeader(userIdHead) final Long userId,
                                       @RequestBody final RequestIncDto requestIncDto) {

        log.info("POST create request; userId={}", userId);
        return requestService.createRequest(userId, requestIncDto);
    }

    /**
     * Вернуть все запросы пользователя с ответами на них
     *
     * @param userId id пользователя
     * @return список запросов с ответами
     */
    @GetMapping
    public List<RequestWithItemDto> getRequestsUser(@RequestHeader(userIdHead) final Long userId) {

        log.info("GET requests; userId={}", userId);
        return requestService.getRequestsUser(userId);
    }

    /**
     * Вернуть все запросы других пользователей
     *
     * @param userId id пользователя
     * @param from   индекс страницы
     * @param size   размер страницы
     * @return список запросов пользователей
     */
    @GetMapping("/all")
    public List<RequestOutDto> getAllRequests(@RequestHeader(userIdHead) final Long userId,
                                              @RequestParam final Integer from,
                                              @RequestParam final Integer size) {

        log.info("GET all requests; userId={}, from={}, size={}", userId, from, size);
        return requestService.getAllRequests(userId, from, size);
    }

    /**
     * Вернуть конкретный запрос
     *
     * @param requestId id запроса
     * @return нужный запрос
     */
    @GetMapping("/{requestId}")
    public RequestWithItemDto getRequest(@PathVariable final Long requestId) {

        log.info("GET request; requestId={}", requestId);
        return requestService.getRequest(requestId);
    }
}
