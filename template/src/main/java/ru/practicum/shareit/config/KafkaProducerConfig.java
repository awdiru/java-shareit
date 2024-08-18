package ru.practicum.shareit.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.practicum.shareit.model.dto.comment.CommentOutDto;
import ru.practicum.shareit.model.dto.item.ItemWithoutCommentsDto;
import ru.practicum.shareit.model.dto.mail.MailWithCommentDto;
import ru.practicum.shareit.model.dto.mail.MailWithItemDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public KafkaTemplate<String, MailWithItemDto> mailWithItemDtoKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(getConfigProps()));
    }

    @Bean
    public KafkaTemplate<String, MailWithCommentDto> mailWithCommentDtoKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(getConfigProps()));
    }

    private Map<String, Object> getConfigProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }
}
