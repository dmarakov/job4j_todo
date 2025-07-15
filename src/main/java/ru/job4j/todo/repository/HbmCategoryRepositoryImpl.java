package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HbmCategoryRepositoryImpl implements CategoryRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Category> getAll() {
        return crudRepository.query("from Category order by id asc", Category.class);
    }

    @Override
    public Category save(Category category) {
        crudRepository.run(session -> session.persist(category));
        return category;
    }

    @Override
    public List<Category> findByIds(List<Integer> ids) {
        return crudRepository.query(
                "from Category where id in (:ids)", Category.class,
                Map.of("ids", ids)
        );
    }
}
