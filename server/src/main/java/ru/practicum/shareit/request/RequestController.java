package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.model.dto.request.RequestIncDto;
import ru.practicum.shareit.model.dto.request.RequestOutDto;
import ru.practicum.shareit.model.dto.request.RequestWithItemDto;
import ru.practicum.shareit.request.annotations.RequestControllerExceptionHandler;

import java.util.List;

import static ru.practicum.shareit.constants.Headers.USER_ID_HEADER;

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

    /**
     * Создать запрос на вещь
     *
     * @param userId        id пользователя
     * @param requestIncDto запрос на вещь
     * @return ответ сервера
     */
    @PostMapping
    public RequestOutDto createRequest(@RequestHeader(USER_ID_HEADER) final Long userId,
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
    public List<RequestWithItemDto> getRequestsUser(@RequestHeader(USER_ID_HEADER) final Long userId) {

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
    public List<RequestOutDto> getAllRequests(@RequestHeader(USER_ID_HEADER) final Long userId,
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
