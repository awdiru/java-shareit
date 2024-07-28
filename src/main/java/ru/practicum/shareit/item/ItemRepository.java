package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс репозитория для работы с таблицей Items
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Вернуть вещь по id
     *
     * @param id id вещи
     * @return искомая вещь
     */
    Item findById(long id);

    /**
     * Вернуть список вещей пользователя
     *
     * @param ownerId id пользователя
     * @return список вещей пользователя
     */
    List<Item> findAllByOwnerId(Long ownerId);

    /**
     * Поиск вещи по содержанию в названии или описании текста
     *
     * @param text  текст для поиска по названию
     * @param text2 текст для поиска по описанию
     * @return список найденных вещей
     */
    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String text, String text2);
}
