package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.BookingStatusEnum;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Получение бронирования по идентификатору
     *
     * @param bookingId идентификатор бронирования
     * @return бронь
     */
    Booking findById(long bookingId);

    /**
     * Получение списка брони для статуса ALL (все)
     *
     * @param userId идентификатор пользователя
     * @return список брони
     */
    @Query("select b " +
            "from Booking as b " +
            "join User as u on b.booker.id = u.id " +
            "where u.id = :userId " +
            "order by b.start desc")
    List<Booking> findAllByBooker(long userId);

    /**
     * Получение списка брони для статуса CURRENT (текущие)
     *
     * @param userId идентификатор пользователя
     * @param now    текущее время
     * @return список брони
     */
    @Query("select b " +
            "from Booking as b " +
            "join User as u on b.booker.id = u.id " +
            "where u.id = :userId " +
            "and b.start < :now " +
            "and b.end > :now " +
            "order by b.start")
    List<Booking> findAllByBookerForStatusCurrent(long userId, LocalDateTime now);

    /**
     * Получение списка брони для статуса PAST (завершённые)
     *
     * @param userId идентификатор пользователя
     * @param now    конец брони
     * @return список брони
     */
    @Query("select b " +
            "from Booking as b " +
            "join User as u on b.booker.id = u.id " +
            "where u.id = :userId " +
            "and b.end < :now " +
            "order by b.start desc")
    List<Booking> findAllByBookerForStatusPast(long userId, LocalDateTime now);

    /**
     * Получение списка брони для статуса FUTURE (будущие)
     *
     * @param userId идентификатор пользователя
     * @param now    начало брони
     * @return список брони
     */

    @Query("select b " +
            "from Booking as b " +
            "join User as u on b.booker.id = u.id " +
            "where u.id = :userId " +
            "and b.start > :now " +
            "order by b.start desc")
    List<Booking> findAllByBookerForStatusFuture(long userId, LocalDateTime now);

    /**
     * Получение списка брони для статуса WAITING или REJECTED (ожидающие подтверждения и отклонённые)
     *
     * @param userId идентификатор пользователя
     * @param status статус WAITING или REJECTED
     * @return список брони
     */
    @Query("select b " +
            "from Booking as b " +
            "join User as u on b.booker.id = u.id " +
            "where u.id = :userId " +
            "and b.status = :status " +
            "order by b.start desc")
    List<Booking> findAllByBookerForStatusWaitingOrRejected(long userId, BookingStatusEnum status);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     *
     * @param userId идентификатор пользователя
     */
    @Query("select b " +
            "from Booking as b " +
            "join Item as it on b.item.id = it.id " +
            "join User as u on it.owner.id = u.id " +
            "where u.id = :userId " +
            "order by b.start desc")
    List<Booking> findAllBookingsItemsUser(long userId);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     * для модификатора CURRENT
     *
     * @param userId идентификатор пользователя
     */
    @Query("select b " +
            "from Booking as b " +
            "join Item as it on b.item.id = it.id " +
            "join User as u on it.owner.id = u.id " +
            "where u.id = :userId " +
            "and b.start < :now " +
            "and b.end > :now " +
            "order by b.start desc")
    List<Booking> findAllBookingsItemsUserForStatusCurrent(long userId, LocalDateTime now);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     * для модификатора PAST
     *
     * @param userId идентификатор пользователя
     */
    @Query("select b " +
            "from Booking as b " +
            "join Item as it on b.item.id = it.id " +
            "join User as u on it.owner.id = u.id " +
            "where u.id = :userId " +
            "and b.end < :now " +
            "order by b.start desc")
    List<Booking> findAllBookingsItemsUserForStatusPast(long userId, LocalDateTime now);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     * для модификатора FUTURE
     *
     * @param userId идентификатор пользователя
     */
    @Query("select b " +
            "from Booking as b " +
            "join Item as it on b.item.id = it.id " +
            "join User as u on it.owner.id = u.id " +
            "where u.id = :userId " +
            "and b.start > :now " +
            "order by b.start desc")
    List<Booking> findAllBookingsItemsUserForStatusFuture(long userId, LocalDateTime now);

    /**
     * Посмотреть все запросы на бронирования владельцу вещей
     * для модификатора WAITING или REJECTED
     *
     * @param userId идентификатор пользователя
     */
    @Query("select b " +
            "from Booking as b " +
            "join Item as it on b.item.id = it.id " +
            "join User as u on it.owner.id = u.id " +
            "where u.id = :userId " +
            "and b.status = :status " +
            "order by b.start desc")
    List<Booking> findAllBookingsItemsUserForStatusWaitingOrRejected(long userId, BookingStatusEnum status);

    /**
     * Посмотреть последние бронирование вещи пользователя
     *
     * @param itemId id вещи
     * @param now    точка во времени
     * @return последнее бронирование для заданной точки
     */
    @Query(value = "select * " +
            "from bookings as b " +
            "where b.item = :itemId " +
            "and b.status = 'APPROVED' " +
            "and b.start_time < :now " +
            "order by b.end_time desc " +
            "limit 1", nativeQuery = true)
    Booking findLastBooking(Long itemId, LocalDateTime now);

    /**
     * Посмотреть следующее бронирование вещи пользователя
     *
     * @param itemId id вещи
     * @param now    точка во времени
     * @return следующее бронирование для заданной точки
     */
    @Query(value = "select * " +
            "from bookings as b " +
            "where b.item = :itemId " +
            "and b.start_time > :now " +
            "and b.status <> 'REJECTED' " +
            "order by b.start_time " +
            "limit 1", nativeQuery = true)
    Booking findNextBooking(Long itemId, LocalDateTime now);

    /**
     * Поиск бронирования по id пользователя и вещи
     *
     * @param bookerId id пользователя
     * @param itemId   id вещи
     * @param now      точка во времени
     * @return последнее бронирование пользователя для заданной точки
     */
    @Query(value = "select * " +
            "from bookings as b " +
            "where b.item = :itemId " +
            "and b.booker = :bookerId " +
            "and b.start_time < :now " +
            "order by b.start_time desc " +
            "limit 1", nativeQuery = true)
    Booking searchForBookerIdAndItemId(Long bookerId, Long itemId, LocalDateTime now);
}
