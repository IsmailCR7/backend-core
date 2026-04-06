package ru.mentee.power.crm.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Lead addLead(String email, String company, LeadStatus status) {
        Optional<Lead> existing = repository.findByEmail(email);
        if (existing.isPresent()) {
            throw new IllegalStateException("Lead with email already exists: " + email);
        }
        Lead lead = new Lead(
                UUID.randomUUID(),
                email,
                company,
                status
        );
        return repository.save(lead);
    }

    public Lead update(UUID id, Lead updateLead) {
        Optional <Lead> existing = repository.findById(id);
        if (existing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find lead with id" + id);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find lead with id" + id );
        }
        repository.delete(id);

    }


    public List<Lead> findAll() {

        return new ArrayList<Lead>(repository.findAll());
    }
    public List<Lead> findByStatus(LeadStatus status){
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
}
