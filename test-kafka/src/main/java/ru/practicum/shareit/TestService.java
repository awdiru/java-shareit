package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.model.dto.comment.CommentOutDto;
import ru.practicum.shareit.model.dto.item.ItemWithoutCommentsDto;
import ru.practicum.shareit.model.dto.mail.MailWithCommentDto;
import ru.practicum.shareit.model.dto.mail.MailWithItemDto;

import static ru.practicum.shareit.constants.TopicNames.ADD_COMMENT;
import static ru.practicum.shareit.constants.TopicNames.CREATING_ITEM_ON_REQUESTS;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    @KafkaListener(topics = CREATING_ITEM_ON_REQUESTS, groupId = "group_id")
    public void createItemOnRequest(MailWithItemDto mail) {
        log.info("Create User" + mail.toString());
    }

    @KafkaListener(topics = ADD_COMMENT, groupId = "group_id")
    public void addComment(MailWithCommentDto mail) {
        log.info("Add Comment " + mail.toString());
    }
/*
    public void send(MailWithCommentDto mail) {
        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(mail.getTo());
        simpleMailMessage.setText(mail.getMessage());
        simpleMailMessage.setSubject(mail.getSubject());

        log.info("Sending email: {}", simpleMailMessage);

        mailSender.send(simpleMailMessage);
    }*/
}
