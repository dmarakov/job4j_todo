package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category save(Category category);

    List<Category> findByIds(List<Integer> ids);
}
