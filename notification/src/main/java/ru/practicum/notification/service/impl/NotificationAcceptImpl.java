package ru.practicum.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.notification.service.EmailSender;
import ru.practicum.notification.service.NotificationAccept;
import ru.practicum.shareit.model.Mail;


@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationAcceptImpl implements NotificationAccept {
    private final EmailSender emailSender;


    @KafkaListener(topics = "ADD_COMMENT_TOPIC", groupId = "group_id")
    @Override
    public void acceptPostCommentNotification(Mail mail) {
        log.info("Notification comment: {}", mail);
        emailSender.send(mail);
    }

    @KafkaListener(topics = "CREATING_ITEM_ON_REQUESTS_NOTIFICATION", groupId = "group_id")
    @Override
    public void acceptItemOnRequestNotification(Mail mail) {
        log.info("Notification item: {}", mail);
        emailSender.send(mail);
    }

    @KafkaListener(topics = "BOOKING_STATUS_TOPIC", groupId = "group_id")
    @Override
    public void acceptBookingStatusNotification(Mail mail) {
        log.info("Notification booking status: {}", mail);
        emailSender.send(mail);
    }

}
