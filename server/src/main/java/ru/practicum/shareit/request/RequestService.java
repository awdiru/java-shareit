package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {
    /**
     * Создание нового запроса
     *
     * @param userId        id пользователя
     * @param requestIncDto Данные нового запроса
     * @return новый запрос
     */
    RequestOutDto createRequest(Long userId, RequestIncDto requestIncDto);

    /**
     * Вернуть все запросы пользователя
     *
     * @param userId id пользователя
     * @return все запросы пользователя
     */
    List<RequestWithItemDto> getRequestsUser(final Long userId);

    /**
     * Вернуть все запросы других людей
     *
     * @param userId id пользователя
     * @param from   индекс страницы
     * @param size   размер страницы
     * @return страница № from размером size с запросами других пользователей
     */
    List<RequestOutDto> getAllRequests(final Long userId,
                                       final Integer from,
                                       final Integer size);

    /**
     * Вернуть запрос по id
     *
     * @param requestId id запроса
     * @return нужный запрос
     */
    RequestWithItemDto getRequest(final Long requestId);
}