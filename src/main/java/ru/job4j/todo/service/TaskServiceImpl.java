package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    public final TaskRepository repository;

    @Override
    public Task save(Task task) {
        String zoneId = task.getUser().getTimeZone() != null ? task.getUser().getTimeZone()
                : TimeZone.getDefault().getID();
        ZoneId zone = ZoneId.of(zoneId);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zone);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        task.setCreated(localDateTime);
        return repository.save(task);
    }

    @Override
    public boolean deleteById(int id) {
        return repository.deleteById(id);
    }

    @Override
    public boolean update(Task task) {
        task.setCreated(LocalDateTime.now());
        return repository.update(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Collection<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public Collection<Task> findResolved() {
        return repository.findAll()
                .stream()
                .filter(Task::isDone)
                .toList();
    }

    @Override
    public Collection<Task> findNew() {
        return repository.findAll()
                .stream()
                .filter(task -> !task.isDone())
                .toList();
    }

    @Override
    public boolean markDone(int id) {
        Optional<Task> task = findById(id);
        if (task.isPresent()) {
            task.get().setDone(true);
            return update(task.get());
        }
        return false;
    }
}
