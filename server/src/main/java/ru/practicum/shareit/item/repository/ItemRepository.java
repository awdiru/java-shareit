package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.model.dto.item.ItemToRequestDto;

import java.util.List;

/**
 * Интерфейс репозитория для работы с таблицей Items
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Вернуть список вещей пользователя
     *
     * @param ownerId id пользователя
     * @return список вещей пользователя
     */
    Slice<Item> findAllByOwnerId(Long ownerId, Pageable paging);

    /**
     * Поиск вещи по содержанию в названии или описании текста
     *
     * @param text текст для поиска по названию
     * @return список найденных вещей
     */
    @Query("""
            select it
            from Item as it
            where it.available = true
            and (lower(it.name) like :text
            or lower(it.description) like :text)
            """)
    List<Item> searchByNameOrDescription(String text);

    /**
     * Вернуть все предметы, созданные по запросу
     *
     * @param requestId id запроса
     * @return список предметов, созданных по запросу
     */
    @Query("""
            select i.id as id, i.name as name, i.owner.id as ownerId
            from Item as i
            where i.request.id = :requestId
            """)
    List<ItemToRequestDto> findAllByRequestId(Long requestId);
}