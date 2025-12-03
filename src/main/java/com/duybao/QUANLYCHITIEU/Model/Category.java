package com.duybao.QUANLYCHITIEU.Model;

import com.duybao.QUANLYCHITIEU.Enum.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "categories")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"users"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true) // thêm unique
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private String iconUrl;
    private String color;

    @ManyToMany(mappedBy = "categories")
    private Set<User> users = new HashSet<>();
}
