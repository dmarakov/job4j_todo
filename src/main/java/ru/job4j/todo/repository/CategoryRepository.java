package ru.job4j.todo.repository;

import ru.job4j.todo.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> getAll();

    Category save(Category category);

    List<Category> findByIds(List<Integer> id);
}
