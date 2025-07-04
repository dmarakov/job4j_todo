package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private String login;
    private String password;
}
