package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HmbPriorityRepositoryImpl implements PriorityRepository {

    private final StandardServiceRegistry registry;
    private final SessionFactory sf;

    public HmbPriorityRepositoryImpl(SessionFactory sf) {
        this.sf = sf;
        this.registry = new StandardServiceRegistryBuilder().configure().build();
    }

    @Override
    public List<Priority> getAll() {
        Session session = sf.openSession();
        List<Priority> rsl = new ArrayList<>();
        try {
            session.getTransaction();
            rsl = session.createQuery(
                    "FROM Priority", Priority.class
            ).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Priority save(Priority priority) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(priority);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return priority;
    }
}
