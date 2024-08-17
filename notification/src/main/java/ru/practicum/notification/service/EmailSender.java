package ru.practicum.notification.service;

import lombok.Value;
import ru.practicum.shareit.model.Mail;


public interface EmailSender {
    void send(final Mail mail);
}
