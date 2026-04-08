package ru.mentee.power.crm.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@Service
public class LeadService {

    private final LeadRepository repository;
    private static final Logger log = LoggerFactory.getLogger(LeadService.class);

    public LeadService(LeadRepository repository) {
        this.repository = repository;
        log.info("LeadService constructor called");
    }

    @PostConstruct
    void init() {
        log.info("LeadService @PostConstruct init() called - Bean lifecycle phase");
    }

    // Принимает объект Lead (для использования из контроллера)
    public Lead addLead(Lead lead) {
        Optional<Lead> existing = repository.findByEmail(lead.email());
        if (existing.isPresent()) {
            throw new IllegalStateException("Lead with email already exists: " + lead.email());
        }
        Lead newLead = new Lead(
                UUID.randomUUID(),
                lead.email(),
                lead.company(),
                lead.status()
        );
        return repository.save(newLead);
    }

    // Перегруженный метод для обратной совместимости
    public Lead addLead(String email, String company, LeadStatus status) {
        return addLead(new Lead(email, company, status));
    }

    public Lead update(UUID id, Lead updateLead) {
        Optional<Lead> existing = repository.findById(id);
        if (existing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find lead with id: " + id);
        }
        Lead updLead = new Lead(
                id,
                updateLead.email(),
                updateLead.company(),
                updateLead.status()
        );
        return repository.save(updLead);
    }

    public void delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find lead with id: " + id);
        }
        repository.delete(id);
    }

    public List<Lead> findAll() {
        return new ArrayList<>(repository.findAll());
    }

    public List<Lead> findByStatus(LeadStatus status) {
        return repository.findAll().stream()
                .filter(lead -> lead.status().equals(status))
                .collect(Collectors.toList());
    }

    public Optional<Lead> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Lead> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    // Новый метод для поиска по строке и статусу
    public List<Lead> searchByNameOrEmail(String search, LeadStatus status) {
        List<Lead> allLeads = repository.findAll();
        Stream<Lead> stream = allLeads.stream();

        if (search != null && !search.isBlank()) {
            String searchLower = search.toLowerCase();
            stream = stream.filter(lead ->
                    lead.email().toLowerCase().contains(searchLower) ||
                            (lead.company() != null && lead.company().toLowerCase().contains(searchLower))
            );
        }

        if (status != null) {
            stream = stream.filter(lead -> lead.status().equals(status));
        }

        return stream.collect(Collectors.toList());
    }

    // Существующий метод findLeads (можно оставить для совместимости)
    public List<Lead> findLeads(String email, String company, LeadStatus status) {
        List<Lead> allLeads = repository.findAll();
        Stream<Lead> stream = allLeads.stream();

        if (email != null && !email.isBlank()) {
            stream = stream.filter(lead ->
                    lead.email().toLowerCase().contains(email.toLowerCase()));
        }

        if (company != null && !company.isBlank()) {
            stream = stream.filter(lead ->
                    lead.company() != null &&
                            lead.company().toLowerCase().contains(company.toLowerCase()));
        }

        if (status != null) {
            stream = stream.filter(lead ->
                    lead.status().equals(status));
        }

        return stream.collect(Collectors.toList());
    }
}
