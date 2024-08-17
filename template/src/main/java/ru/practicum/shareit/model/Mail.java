package ru.practicum.shareit.model;


import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.UUID;

@ToString
@Setter
@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Mail {
    public final String id = UUID.randomUUID().toString();
    @Email
    private String from;
    @Email
    private String to;
    private String message;
    private String subject;
}
