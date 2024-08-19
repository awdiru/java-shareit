package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Rating;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    /**
     * Вернуть рейтинг вещи
     *
     * @param itemId id вещи
     * @return рейтинг вещи
     */
    @Query("""
            select avg(r.rating)
            from Rating as r
            where r.item = :itemId
            """)
    Double getRatingItem(Long itemId);

    /**
     * Вернуть оценку пользователя, которую он ставил вещи.
     * @param author id пользователя
     * @param item id вещи
     * @return оценка пользователя
     */
    Optional<Rating> findByAuthorAndItem(Long author, Long item);
}
