package ru.practicum.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.practicum.notification.service.EmailSender;
import ru.practicum.shareit.model.Mail;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    @Override
    public void send(Mail mail) {
        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(mail.getTo());
        simpleMailMessage.setText(mail.getMessage());
        simpleMailMessage.setSubject(mail.getSubject());

        log.info("Sending email: {}", simpleMailMessage);

        mailSender.send(simpleMailMessage);
    }
}
