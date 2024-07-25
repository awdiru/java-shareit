package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findById(long id);

    List<Item> findAllByOwnerId(long ownerId);

    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String text, String text2);
}
