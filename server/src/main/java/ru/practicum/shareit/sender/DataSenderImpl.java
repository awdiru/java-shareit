package ru.practicum.shareit.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.model.Mail;
import ru.practicum.shareit.model.enums.BookingStatusEnum;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSenderImpl implements DataSender {
    private static final String commentSubject = "Ваш предмет прокомментировали";
    private static final String itemSubject = "Появился предмет на ваш запрос";
    private static final String bookingSubject = "Владелец предмета принял решение об вашем запросе";

    private final KafkaTemplate<String, Mail> template;

    private String topic;


    @Override
    public void sendCommentNotification(final String topic,
                                        final String author,
                                        final String itemName,
                                        final String text,
                                        final String owner) {
        this.topic = topic;
        var mail = Mail.builder()
                .from(author)
                .to(owner)
                .subject(commentSubject)
                .message(String.format(
                        "Пользователь {} на ваш предмет {} оставил следующий комментарий: {}",
                        author, itemName, text
                ))
                .build();
        send(mail);
    }

    @Override
    public void sendItemCreatedNotification(final String topic,
                                            final String owner,
                                            final String description,
                                            final String requester) {
        this.topic = topic;
        var mail = Mail.builder()
                .from(owner)
                .to(requester)
                .subject(itemSubject)
                .message(String.format(
                        "Пользователь {} выставил на аренду предмет по вашему запросу: {}",
                        owner, description
                ))
                .build();
        send(mail);
    }

    @Override
    public void sendBookingStatusNotification(final String topic,
                                              final String ownerEmail,
                                              final BookingStatusEnum status,
                                              final String itemName,
                                              final String bookerEmail) {
        this.topic = topic;
        var result = status.equals(BookingStatusEnum.APPROVED) ? "одобрил":"отменил";
        var mail = Mail.builder()
                .from(ownerEmail)
                .to(bookerEmail)
                .subject(bookingSubject)
                .message(String.format(
                        "Пользователь {} {} вашу заявку на предмет {}",
                        ownerEmail, result, itemName
                ))
                .build();
        send(mail);
    }

    @Override
    public void send(final Mail mail) {
        try {
            log.info("value:{}", mail);
            template.send(topic, mail)
                    .whenComplete(
                            (result, ex) -> {
                                if (ex == null) {
                                    log.info(
                                            "message id:{} was sent, offset:{}",
                                            mail.getId(),
                                            result.getRecordMetadata().offset());
                                } else {
                                    log.error("message id:{} was not sent", mail.getId(), ex);
                                }
                            });
        } catch (Exception ex) {
            log.error("send error, value:{}", mail, ex);
        }
    }
}
