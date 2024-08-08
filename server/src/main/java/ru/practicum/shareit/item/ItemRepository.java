package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    Page<Item> findAllByOwnerId(Long ownerId, Pageable paging);

    /**
     * Поиск вещи по содержанию в названии или описании текста
     *
     * @param text текст для поиска по названию
     * @return список найденных вещей
     */
    @Query("select it " +
            "from Item as it " +
            "where it.available = true " +
            "and (lower(it.name) like :text " +
            "or lower(it.description) like :text) ")
    List<Item> searchByNameOrDescription(String text);
}
