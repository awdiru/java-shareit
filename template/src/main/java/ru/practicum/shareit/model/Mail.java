package ru.practicum.shareit.model;


import jakarta.validation.constraints.Email;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@ToString
@Setter
@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Mail implements Serializable{
    public String id = UUID.randomUUID().toString();
    @Email
    private String from;
    @Email
    private String to;
    private String message;
    private String subject;
}
