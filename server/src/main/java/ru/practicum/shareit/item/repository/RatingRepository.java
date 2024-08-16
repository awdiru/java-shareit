package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Rating;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("""
            select avg(r.rating)
            from Rating as r
            where r.item = :itemId
            """)
    Double getRatingItem(Long itemId);

    Optional<Rating> findByAuthorAndItem(Long author, Long item);
}
