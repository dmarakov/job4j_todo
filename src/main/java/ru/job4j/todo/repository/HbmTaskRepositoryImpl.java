package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class HbmTaskRepositoryImpl implements TaskRepository, AutoCloseable {
    private final StandardServiceRegistry registry;
    private final SessionFactory sf;

    public HbmTaskRepositoryImpl(SessionFactory sf) {
        this.registry = new StandardServiceRegistryBuilder().configure().build();
        this.sf = sf;
    }

    @Override
    public Task save(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            task.setCreated(LocalDateTime.now());
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        Session session = sf.openSession();
        boolean rsl = false;
        try {
            session.beginTransaction();
            Query query = session.createQuery(
                            "DELETE Task WHERE id = :fId")
                    .setParameter("fId", id);
            rsl = query.executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public boolean update(Task task) {
        Session session = sf.openSession();
        boolean rsl = false;
        try {
            session.beginTransaction();
            Query query = session.createQuery(
                            "UPDATE Task SET description = :fDescription, created = :fCreated, done = :fDone"
                                    + " WHERE id = :fId")
                    .setParameter("fId", task.getId())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone());
            rsl = query.executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        Optional<Task> rsl = Optional.empty();
        try {
            session.getTransaction();
            rsl = session.createQuery("FROM Task WHERE id = :fId", Task.class)
                    .setParameter("fId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Collection<Task> findAll() {
        Session session = sf.openSession();
        List<Task> rsl = new ArrayList<>();
        try {
            session.beginTransaction();
            rsl = session.createQuery("FROM Task", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
