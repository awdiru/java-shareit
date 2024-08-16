package ru.practicum.shareit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "ShareIt",
                description = "Приложение для возможности бронирования вещей и сдачи их в аренду",
                version = "0.0.1",
                contact = @Contact(
                        name = "Авдонин Евгений Сергеевич",
                        email = "awdiru@gmail.com"
                )
        )
)
public class OpenApiConfig {

}
