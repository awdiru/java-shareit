package ru.practicum.shareit.sender;

import ru.practicum.shareit.model.Mail;
import ru.practicum.shareit.model.enums.BookingStatusEnum;


public interface DataSender {

    void sendCommentNotification(final String topic, final String author, final String itemName, final String text, final String owner);

    void sendItemCreatedNotification(final String topic, final String email, final String description, final String requester);

    void sendBookingStatusNotification(final String topic, final String ownerEmail, final BookingStatusEnum status, String itemName, String bookerEmail);

    void send(final Mail mail);
}
