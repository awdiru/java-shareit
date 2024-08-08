package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Шаблон Comment для работы с базой данных
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @Column(name = "text")
    private String text;

    @Column(name = "created")
    private LocalDateTime created;
}