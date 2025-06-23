package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HbmTaskRepositoryImplTest {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    private HbmTaskRepositoryImpl repository;

    @BeforeAll
    static void init() {
        registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        repository = new HbmTaskRepositoryImpl(sessionFactory);
    }

    @AfterEach
    void tearDown() throws Exception {
        var session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Task").executeUpdate();
        session.getTransaction().commit();
        session.close();
        repository.close();
    }

    @AfterAll
    static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    void whenSaveAndFindByIdThenOk() {
        Task task = new Task();
        task.setDescription("Test Task");
        task.setDone(false);

        Task saved = repository.save(task);
        Optional<Task> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getDescription()).isEqualTo("Test Task");
        assertThat(result.get().isDone()).isFalse();
        assertThat(result.get().getCreated()).isNotNull();
    }

    @Test
    void whenUpdateThenFieldsUpdated() {
        Task task = repository.save(new Task(3, "Original", null, false));
        task.setDescription("Updated");
        task.setDone(true);

        boolean updated = repository.update(task);
        Optional<Task> result = repository.findById(task.getId());

        assertThat(updated).isTrue();
        assertThat(result).isPresent();
        assertThat(result.get().getDescription()).isEqualTo("Updated");
        assertThat(result.get().isDone()).isTrue();
    }

    @Test
    void whenDeleteByIdThenTaskRemoved() {
        Task task = repository.save(new Task(0, "To be deleted", null, false));
        boolean deleted = repository.deleteById(task.getId());
        Optional<Task> result = repository.findById(task.getId());

        assertThat(deleted).isTrue();
        assertThat(result).isEmpty();
    }

    @Test
    void whenFindAllThenReturnAll() {
        repository.save(new Task(1, "Task 1", null, false));
        repository.save(new Task(2, "Task 2", null, true));

        List<Task> all = (List<Task>) repository.findAll();
        assertThat(all).hasSize(2);
    }
}
