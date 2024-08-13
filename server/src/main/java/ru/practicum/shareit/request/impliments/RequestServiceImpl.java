package ru.practicum.shareit.request.impliments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectRequestIdException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
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
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final ItemMapper itemMapper;

    @Override
    public RequestOutDto createRequest(final Long userId,
                                       final RequestIncDto requestIncDto) {

        Request request = requestMapper.toRequestFromRequestIncDto(requestIncDto);
        request.setRequestor(userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден")));

        request.setCreated(LocalDateTime.now());
        return requestMapper.toRequestOutDtoFromRequest(requestRepository.save(request));
    }

    @Override
    public List<RequestWithItemDto> getRequestsUser(final Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден"));
        return requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId)
                .stream()
                .map(requestMapper::toRequestWithItemDtoFromRequest)
                .peek(requestWithItemDto ->
                        requestWithItemDto
                                .setItems(itemRepository.findAllByRequestId(requestWithItemDto.getId())))
                .toList();
    }

    @Override
    public List<RequestOutDto> getAllRequests(final Long userId,
                                              final Integer from,
                                              final Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IncorrectUserIdException("Пользователь с id " + userId + " не найден"));
        Pageable paging = PageRequest.of(from, size);
        return requestRepository.getAllRequests(userId, paging)
                .stream()
                .map(requestMapper::toRequestOutDtoFromRequest)
                .toList();
    }

    @Override
    public RequestWithItemDto getRequest(final Long requestId) {

        RequestWithItemDto request = requestMapper.toRequestWithItemDtoFromRequest(requestRepository.findById(requestId)
                .orElseThrow(() -> new IncorrectRequestIdException("Запрос с id " + requestId + " не найден")));

        request.setItems(itemRepository.findAllByRequestId(requestId));
        return request;
    }
}
