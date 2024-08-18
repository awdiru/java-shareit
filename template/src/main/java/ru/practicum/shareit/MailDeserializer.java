package ru.practicum.shareit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import ru.practicum.shareit.model.Mail;

import java.io.IOException;
import java.util.Map;

public class MailDeserializer implements Deserializer<Mail> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Ничего не делаем в configure
    }

    @Override
    public Mail deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            JsonNode root = objectMapper.readTree(data);
            // Устанавливаем ID
            String id = root.path("id").asText();
            String from = root.path("from").asText();
            String to = root.path("to").asText();
            String message = root.path("message").asText();
            String subject = root.path("subject").asText();

            Mail mail = new Mail(id, from, to, message, subject);

            return mail;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка десериализации сообщения из Kafka: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        // Ничего не делаем в close
    }
}