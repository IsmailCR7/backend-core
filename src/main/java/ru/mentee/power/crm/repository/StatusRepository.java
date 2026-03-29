package ru.mentee.power.crm.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.model.Status;

@Repository
public class StatusRepository {
    private final Set<Status> statuses = new HashSet<>();

    public void addStatus(Status status) {
        statuses.add(status);
    }

    public void removeStatus(Status status) {
        statuses.remove(status);
    }

    public void removeStatusByName(String name) {
        statuses.removeIf(status -> status.name().equals(name));
    }

    public List<Status> findAll() {
        return new ArrayList<>(statuses);
    }

    public boolean containsStatus(String name) {
        return statuses.stream()
                .anyMatch(status -> status.name().equalsIgnoreCase(name));
    }

    public boolean containsStatus(Status status) {
        return statuses.contains(status);
    }

    public Status findByName(String name) {
        return statuses.stream()
                .filter(status -> status.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}