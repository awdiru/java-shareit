package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Интерфейс репозитория для работы с таблицей Users
 */
public interface UserRepository extends JpaRepository<User, Long> {
}