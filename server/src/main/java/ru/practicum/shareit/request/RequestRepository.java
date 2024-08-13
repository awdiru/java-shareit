package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    /**
     * Вернуть все запросы пользователя
     *
     * @param requestorId id пользователя
     * @return все запросы пользователя
     */

    List<Request> findAllByRequestorIdOrderByCreatedDesc(Long requestorId);

    /**
     * Вернуть все запросы других пользователей
     *
     * @param userId id пользователя
     * @param paging параметры страницы
     * @return страница с запросами других пользователей
     */
    @Query("""
            select r
            from Request as r
            where r.requestor.id <> :userId
            order by r.created
            """)
    Slice<Request> getAllRequests(Long userId, Pageable paging);
}