package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Интерфейс репозитория для работы с таблицей Users
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Вернуть пользователя по id
     *
     * @param id id пользователя
     * @return запрашиваемый пользователь
     */
    User findById(long id);

    /**
     * Удалить пользователя по id
     *
     * @param id id удаляемого пользователя
     * @return удаленный пользователь
     */
    User deleteById(long id);
}
