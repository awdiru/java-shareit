package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "item")
    Long item;

    @Column(name = "author")
    Long author;

    @Column(name = "rating")
    Integer rating;
}
