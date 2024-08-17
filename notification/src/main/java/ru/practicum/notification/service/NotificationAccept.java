package ru.practicum.notification.service;


import ru.practicum.shareit.model.Mail;

public interface NotificationAccept {

    void acceptPostCommentNotification(Mail mail);

    void acceptItemOnRequestNotification(Mail mail);

    void acceptBookingStatusNotification(Mail mail);
}
