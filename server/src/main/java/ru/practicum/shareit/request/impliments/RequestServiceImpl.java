package ru.practicum.shareit.request.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.IncorrectRequestIdException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.dto.model.RequestIncDto;
import ru.practicum.shareit.request.dto.model.RequestOutDto;
import ru.practicum.shareit.request.dto.model.RequestWithItemDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository reposRequest;
    private final ItemRepository reposItem;
    private final UserRepository reposUser;
    private final RequestMapper requestMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public RequestServiceImpl(final RequestRepository reposRequest,
                              final ItemRepository reposItem,
                              final UserRepository reposUser,
                              final RequestMapper requestMapper,
                              final ItemMapper itemMapper) {
        this.reposRequest = reposRequest;
        this.reposItem = reposItem;
        this.reposUser = reposUser;
        this.requestMapper = requestMapper;
        this.itemMapper = itemMapper;
    }

    @Override
    public RequestOutDto createRequest(final Long userId,
                                       final RequestIncDto requestIncDto) {

        Request request = requestMapper.toRequestFromRequestIncDto(requestIncDto);
        request.setRequestor(reposUser.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден")));

        request.setCreated(LocalDateTime.now());
        return requestMapper.toRequestOutDtoFromRequest(reposRequest.save(request));
    }

    @Override
    public List<RequestWithItemDto> getRequestsUser(final Long userId) {

        reposUser.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден"));
        return reposRequest.findAllByRequestorIdOrderByCreatedDesc(userId)
                .stream()
                .map(requestMapper::toRequestWithItemDtoFromRequest)
                .peek(requestWithItemDto ->
                        requestWithItemDto.setItems(
                                reposItem.findAllByRequestId(requestWithItemDto.getId())
                                        .stream()
                                        .map(itemMapper::toItemToRequestFromItem)
                                        .toList()
                        ))
                .toList();
    }

    @Override
    public List<RequestOutDto> getAllRequests(final Long userId,
                                              final Integer from,
                                              final Integer size) {

        Pageable paging = PageRequest.of(from, size);
        return reposRequest.getAllRequests(userId, paging)
                .stream()
                .map(requestMapper::toRequestOutDtoFromRequest)
                .toList();
    }

    @Override
    public RequestWithItemDto getRequest(final Long requestId) {

        RequestWithItemDto request = requestMapper.toRequestWithItemDtoFromRequest(reposRequest.findById(requestId)
                .orElseThrow(() -> new IncorrectRequestIdException("Запрос с id " + requestId + " не найден")));

        request.setItems(
                reposItem.findAllByRequestId(requestId)
                        .stream()
                        .map(itemMapper::toItemToRequestFromItem)
                        .toList()
        );
        return request;
    }
}
