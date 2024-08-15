package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemWithoutCommentsDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    @KafkaListener(topics = "creating-item-on-request", groupId = "group_id")
    public void consume(ItemWithoutCommentsDto itemWithoutCommentsDto) {
        log.info("Create user" + itemWithoutCommentsDto.toString());
    }
}
