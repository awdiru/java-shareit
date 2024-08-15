package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.booking.converter.BookingStatusConverter;
import ru.practicum.shareit.enums.BookingStatusEnum;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Шаблон Booking для работы с базой данных
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "Bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private LocalDateTime start;

    @Column(name = "end_time")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker", nullable = false)
    private User booker;

    @Column(name = "status")
    @Convert(converter = BookingStatusConverter.class)
    private BookingStatusEnum status;
}