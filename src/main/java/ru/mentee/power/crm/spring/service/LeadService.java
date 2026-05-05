package ru.mentee.power.crm.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeadService {

    private final LeadRepository repository;

    @Transactional
    public Lead addLead(String email, String company, LeadStatus status) {
        Optional<Lead> existing = repository.findByEmail(email);
        if (existing.isPresent()) {
            throw new IllegalStateException("Lead with email already exists: " + email);
        }

        Lead lead = new Lead(email, company, status);
        log.info("Saving new lead: {}", lead);
        return repository.save(lead);
    }

    @Transactional
    public Lead addLead(Lead lead) {
        return addLead(lead.getEmail(), lead.getCompany(), lead.getStatus());
    }

    @Transactional
    public Lead update(UUID id, Lead updateLead) {
        Lead existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Can't find lead with id: " + id));

        existing.setEmail(updateLead.getEmail());
        existing.setCompany(updateLead.getCompany());
        existing.setStatus(updateLead.getStatus());

        log.info("Updating lead: {}", existing);
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Can't find lead with id: " + id);
        }
        repository.deleteById(id);
        log.info("Deleted lead with id: {}", id);
    }

    public List<Lead> findAll() {
        return repository.findAll();
    }

    public List<Lead> findByStatus(LeadStatus status) {
        return repository.findByStatus(status);
    }

    public Optional<Lead> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Lead> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<Lead> searchByNameOrEmail(String search, LeadStatus status) {
        List<Lead> allLeads = repository.findAll();

        return allLeads.stream()
                .filter(lead -> {
                    if (search != null && !search.isBlank()) {
                        String searchLower = search.toLowerCase();
                        return lead.getEmail().toLowerCase().contains(searchLower) ||
                                lead.getCompany().toLowerCase().contains(searchLower);
                    }
                    return true;
                })
                .filter(lead -> status == null || lead.getStatus() == status)
                .toList();
    }
}