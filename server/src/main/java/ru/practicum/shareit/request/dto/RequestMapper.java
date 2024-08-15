package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.RequestOutDto;
import ru.practicum.shareit.request.RequestIncDto;
import ru.practicum.shareit.request.RequestWithItemDto;
import ru.practicum.shareit.request.model.Request;

/**
 * Конвертер Request классов
 */
@Component
public class RequestMapper {
    public Request toRequestFromRequestIncDto(RequestIncDto request) {
        return request == null ? null :
                new Request(null,
                        request.getDescription(),
                        null,
                        null);
    }

    public RequestOutDto toRequestOutDtoFromRequest(Request request) {
        return request == null ? null :
                new RequestOutDto(request.getId(),
                        request.getDescription(),
                        request.getCreated());
    }

    public RequestWithItemDto toRequestWithItemDtoFromRequest(Request request) {
        return request == null ? null :
                new RequestWithItemDto(request.getId(),
                        request.getDescription(),
                        request.getCreated(),
                        null);
    }
}