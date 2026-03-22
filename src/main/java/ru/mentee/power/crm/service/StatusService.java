package ru.mentee.power.crm.service;

import org.springframework.stereotype.Service;
import ru.mentee.power.crm.model.Status;
import ru.mentee.power.crm.repository.StatusRepository;

import java.util.List;

@Service
public class StatusService {
    private final StatusRepository repository;

    public StatusService(StatusRepository repository) {
        this.repository = repository;
    }

    public List<Status> findAll() {
        return repository.findAll();
    }

    public boolean containsStatus(String name) {
        return repository.containsStatus(name);
    }

    public void addStatus(Status status) {
        if (!containsStatus(status.name())) {
            repository.addStatus(status);
        }
    }

    public void removeStatus(String name) {
        repository.removeStatusByName(name);
    }

    public Status findByName(String name) {
        return repository.findByName(name);
    }
}