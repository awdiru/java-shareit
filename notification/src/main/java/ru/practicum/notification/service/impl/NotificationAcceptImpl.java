package ru.practicum.notification.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.practicum.notification.service.EmailSender;
import ru.practicum.notification.service.NotificationAccept;
import ru.practicum.shareit.model.Mail;

import java.util.Map;

import static ru.practicum.shareit.constants.TopicNames.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationAcceptImpl implements NotificationAccept {
    private final EmailSender emailSender;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = ADD_COMMENT_TOPIC, groupId = "group_id")
    @Override
    public void acceptPostCommentNotification(Mail mail){
        log.info("Notification comment: {}", mail);
        emailSender.send(mail);
    }

    @KafkaListener(topics = CREATING_ITEM_ON_REQUESTS_NOTIFICATION, groupId = "group_id")
    @Override
    public void acceptItemOnRequestNotification(@Payload String message,
                                                @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
                                                @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                                @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                                Map<String, Object> headers) {
        try {
            // Получаем значение "payload" из сообщения
            JsonNode root = objectMapper.readTree(message);
            JsonNode payloadNode = root.path("payload");

            // Извлекаем данные из payload
            String from = payloadNode.path("from").asText();
            String to = payloadNode.path("to").asText();
            String messageText = payloadNode.path("message").asText();
            String subject = payloadNode.path("subject").asText();

            // Создаем объект Mail
            Mail mail = new Mail("", from, to, messageText, subject);

            // Выводим информацию о сообщении
            System.out.println("Partition ID: " + partitionId);
            System.out.println("Topic: " + topic);
            System.out.println("Timestamp: " + timestamp);
            System.out.println("Headers: " + headers);
            System.out.println("Mail: " + mail);

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = BOOKING_STATUS_TOPIC, groupId = "group_id")
    @Override
    public void acceptBookingStatusNotification(Mail mail) {
        log.info("Notification booking status: {}", mail);
        emailSender.send(mail);
    }

}
