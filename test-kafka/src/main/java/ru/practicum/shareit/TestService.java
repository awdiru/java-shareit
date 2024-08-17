package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.model.dto.comment.CommentOutDto;
import ru.practicum.shareit.model.dto.item.ItemWithoutCommentsDto;

import static ru.practicum.shareit.constants.TopicNames.ADD_COMMENT_TOPIC;
import static ru.practicum.shareit.constants.TopicNames.CREATING_ITEM_ON_REQUESTS_NOTIFICATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    @KafkaListener(topics = CREATING_ITEM_ON_REQUESTS_NOTIFICATION, groupId = "group_id")
    public void createItemOnRequest(ItemWithoutCommentsDto itemWithoutCommentsDto) {
        log.info("Create User" + itemWithoutCommentsDto.toString());
    }

    @KafkaListener(topics = ADD_COMMENT_TOPIC, groupId = "group_id")
    public void addComment(CommentOutDto comment) {
        log.info("Add Comment " + comment.toString());
    }
}
