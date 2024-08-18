package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.model.dto.mail.MailWithCommentDto;
import ru.practicum.shareit.model.dto.mail.MailWithItemDto;

import static ru.practicum.shareit.constants.TopicNames.ADD_COMMENT;
import static ru.practicum.shareit.constants.TopicNames.CREATING_ITEM_ON_REQUESTS;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SendMessage sendMessage;

    @KafkaListener(topics = CREATING_ITEM_ON_REQUESTS, groupId = "group_id")
    public void createItemOnRequest(MailWithItemDto mail) {
        log.info("Create Item {}", mail.toString());

        String subject = "Появился предмет на ваш запрос";
        String message = "Пользователь " + mail.getItem().getOwner().getName() +
                " выставил на аренду предмет по вашему запросу: " + mail.getItem();

        sendMessage.send(mail.getTo(), subject, message);


    }

    @KafkaListener(topics = ADD_COMMENT, groupId = "group_id")
    public void addComment(MailWithCommentDto mail) {
        log.info("Add Comment {}", mail.toString());

        String subject = "К вашей вещи добавлен комментарий";
        String message = "Пользователь " + mail.getComment().getAuthorName() +
                " добавил комментарий: " + mail.getComment();

        sendMessage.send(mail.getTo(), subject, message);
    }
}
