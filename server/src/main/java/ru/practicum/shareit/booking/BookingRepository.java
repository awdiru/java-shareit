package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Получение списка брони для статуса ALL (все)
     *
     * @param userId идентификатор пользователя
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */
    @Query("""
            select b
            from Booking as b
            join User as u on b.booker.id = u.id
            where u.id = :userId
            order by b.start desc
            """)
    Slice<Booking> findAllByBooker(long userId, Pageable paging);

    /**
     * Получение списка брони для статуса CURRENT (текущие)
     *
     * @param userId идентификатор пользователя
     * @param now    текущее время
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */
    @Query("""
            select b
            from Booking as b
            join User as u on b.booker.id = u.id
            where u.id = :userId
            and b.start < :now
            and b.end > :now
            order by b.start
            """)
    Slice<Booking> findAllByBookerForStatusCurrent(long userId, LocalDateTime now, Pageable paging);

    /**
     * Получение списка брони для статуса PAST (завершённые)
     *
     * @param userId идентификатор пользователя
     * @param now    конец брони
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */
    @Query("""
            select b
            from Booking as b
            join User as u on b.booker.id = u.id
            where u.id = :userId
            and b.end < :now
            order by b.start desc
            """)
    Slice<Booking> findAllByBookerForStatusPast(long userId, LocalDateTime now, Pageable paging);

    /**
     * Получение списка брони для статуса FUTURE (будущие)
     *
     * @param userId идентификатор пользователя
     * @param now    начало брони
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */

    @Query("""
            select b
            from Booking as b
            join User as u on b.booker.id = u.id
            where u.id = :userId
            and b.start > :now
            order by b.start desc
            """)
    Slice<Booking> findAllByBookerForStatusFuture(long userId, LocalDateTime now, Pageable paging);

    /**
     * Получение списка брони для статуса WAITING или REJECTED (ожидающие подтверждения и отклонённые)
     *
     * @param userId идентификатор пользователя
     * @param status статус WAITING или REJECTED
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */
    @Query("""
            select b
            from Booking as b
            join User as u on b.booker.id = u.id
            where u.id = :userId
            and b.status = :status
            order by b.start desc
            """)
    Slice<Booking> findAllByBookerForStatusWaitingOrRejected(long userId, BookingStatusEnum status, Pageable paging);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     *
     * @param userId идентификатор пользователя
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */
    @Query("""
            select b
            from Booking as b
            join Item as it on b.item.id = it.id
            join User as u on it.owner.id = u.id
            where u.id = :userId
            order by b.start desc
            """)
    Slice<Booking> findAllBookingsItemsUser(long userId, Pageable paging);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     * для модификатора CURRENT
     *
     * @param userId идентификатор пользователя
     * @param now    точка во времени
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */
    @Query("""
            select b
            from Booking as b
            join Item as it on b.item.id = it.id
            join User as u on it.owner.id = u.id
            where u.id = :userId
            and b.start < :now
            and b.end > :now
            order by b.start desc
            """)
    Slice<Booking> findAllBookingsItemsUserForStatusCurrent(long userId, LocalDateTime now, Pageable paging);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     * для модификатора PAST
     *
     * @param userId идентификатор пользователя
     * @param now    точка во времени
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */
    @Query("""
            select b
            from Booking as b
            join Item as it on b.item.id = it.id
            join User as u on it.owner.id = u.id
            where u.id = :userId
            and b.end < :now
            order by b.start desc
            """)
    Slice<Booking> findAllBookingsItemsUserForStatusPast(long userId, LocalDateTime now, Pageable paging);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     * для модификатора FUTURE
     *
     * @param userId идентификатор пользователя
     * @param now    точка во времени
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */
    @Query("""
            select b
            from Booking as b
            join Item as it on b.item.id = it.id
            join User as u on it.owner.id = u.id
            where u.id = :userId
            and b.start > :now
            order by b.start desc
            """)
    Slice<Booking> findAllBookingsItemsUserForStatusFuture(long userId, LocalDateTime now, Pageable paging);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     * для модификатора WAITING или REJECTED
     *
     * @param userId идентификатор пользователя
     * @param status статус запрос WAITING или REJECT
     * @param paging пагинация для ограничения ответа
     * @return список брони
     */
    @Query("""
            select b
            from Booking as b
            join Item as it on b.item.id = it.id
            join User as u on it.owner.id = u.id
            where u.id = :userId
            and b.status = :status
            order by b.start desc
            """)
    Slice<Booking> findAllBookingsItemsUserForStatusWaitingOrRejected(long userId, BookingStatusEnum status, Pageable paging);

    /**
     * Посмотреть последние бронирование вещи пользователя
     *
     * @param itemId id вещи
     * @param now    точка во времени
     * @param paging пагинация для ограничения ответа
     * @return последнее бронирование для заданной точки
     */
    @Query("""
            select b
            from Booking as b
            where b.item.id = :itemId
            and b.status = 'APPROVED'
            and b.start < :now
            order by b.end desc
            """)
    Slice<Booking> findLastBooking(Long itemId, LocalDateTime now, Pageable paging);

    /**
     * Посмотреть следующее бронирование вещи пользователя
     *
     * @param itemId id вещи
     * @param now    точка во времени
     * @param paging пагинация для ограничения ответа
     * @return следующее бронирование для заданной точки
     */
    @Query("""
            select b
            from Booking as b
            where b.item.id = :itemId
            and b.start > :now
            and b.status <> 'REJECTED'
            order by b.start
            """)
    Slice<Booking> findNextBooking(Long itemId, LocalDateTime now, Pageable paging);

    /**
     * Поиск бронирования по id пользователя и вещи
     *
     * @param bookerId id пользователя
     * @param itemId   id вещи
     * @param now      точка во времени
     * @param paging   пагинация для ограничения ответа не более чем одним результатом
     * @return последнее бронирование пользователя для заданной точки
     */
    @Query("""
            select b
            from Booking as b
            where b.item.id = :itemId
            and b.booker.id = :bookerId
            and b.start < :now
            and b.status = 'APPROVED'
            """)
    Slice<Booking> searchForBookerIdAndItemId(Long bookerId, Long itemId, LocalDateTime now, Pageable paging);
}