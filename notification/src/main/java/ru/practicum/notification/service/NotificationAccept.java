package ru.practicum.notification.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import ru.practicum.shareit.model.Mail;

import java.util.Map;

public interface NotificationAccept {

    void acceptPostCommentNotification(Mail mail);

    void acceptItemOnRequestNotification(@Payload String message,
                                         @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
                                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                         @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                                         Map<String, Object> headers);

    void acceptBookingStatusNotification(Mail mail);
}
