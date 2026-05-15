package ru.mentee.power.crm.spring.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@Service
public class LeadService {
    private static final Logger LOG = LoggerFactory.getLogger(LeadService.class);
    private final LeadRepository repository;

    public LeadService(LeadRepository repository) {
        this.repository = repository;
        LOG.info("LeadService constructor called");
    }

    @PostConstruct
    void init() {
        LOG.info("LeadService @PostConstruct init() called - Bean lifecycle phase");
    }

    // ===== CREATE (добавление) =====

    @Transactional
    public Lead addLead(String name, String email, String company, LeadStatus status) {
        checkEmailUniqueness(email);
        Lead lead = new Lead(name, email, company, status);
        Lead saved = repository.save(lead);
        LOG.info("Created lead with id: {}, email: {}", saved.id(), saved.email());
        return saved;
    }

    @Transactional
    public Lead addLead(String email, String company, LeadStatus status) {
        checkEmailUniqueness(email);
        Lead lead = new Lead(email, company, status);
        Lead saved = repository.save(lead);
        LOG.info("Created lead with id: {}, email: {}", saved.id(), saved.email());
        return saved;
    }

    // ===== UPDATE (обновление) =====

    @Transactional
    public Lead update(UUID id, Lead updatedLead) {
        Lead existing = findLeadByIdOrThrow(id);

        existing.setName(updatedLead.name());
        existing.setEmail(updatedLead.email());
        existing.setCompany(updatedLead.company());
        existing.setStatus(updatedLead.status());

        Lead saved = repository.save(existing);
        LOG.info("Updated lead with id: {}", id);
        return saved;
    }

    // ===== DELETE (удаление) =====

    @Transactional
    public void delete(UUID id) {
        findLeadByIdOrThrow(id);
        repository.deleteById(id);
        LOG.info("Deleted lead with id: {}", id);
    }

    // ===== READ (чтение) — простые методы =====

    public List<Lead> findAll() {
        return repository.findAll();
    }

    public Optional<Lead> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Lead> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    // ===== READ — улучшенные методы =====

    public List<Lead> findByStatus(LeadStatus status) {
        return repository.findByStatus(status);
    }

    public List<Lead> findByCompany(String company) {
        return repository.findByCompany(company);
    }

    public List<Lead> findByEmailContaining(String emailPart) {
        if (emailPart == null || emailPart.isBlank()) {
            return findAll();
        }
        return repository.findByEmailContaining(emailPart);
    }

    public List<Lead> findByStatusAndCompany(LeadStatus status, String company) {
        return repository.findByStatusAndCompany(status, company);
    }

    public long countByStatus(LeadStatus status) {
        return repository.countByStatus(status);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public List<Lead> findByStatuses(List<LeadStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return findAll();
        }
        return repository.findByStatusIn(statuses);
    }

    public List<Lead> findCreatedAfter(LocalDateTime date) {
        if (date == null) {
            return findAll();
        }
        return repository.findCreatedAfter(date);
    }

    public List<Lead> findByCompanyOrderedByDate(String company) {
        return repository.findByCompanyOrderedByDate(company);
    }

    // ===== ПАГИНАЦИЯ =====

    public Page<Lead> findAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findAll(pageable);
    }

    public Page<Lead> findByStatusPaged(LeadStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByStatus(status, pageable);
    }

    public Page<Lead> findByCompanyPaged(String company, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByCompany(company, pageable);
    }

    // ===== BULK ОПЕРАЦИИ =====

    @Transactional
    public int convertNewToContacted() {
        int updated = repository.updateStatusBulk(LeadStatus.NEW, LeadStatus.CONTACTED);
        LOG.info("Bulk update: converted {} leads from NEW to CONTACTED", updated);
        return updated;
    }

    @Transactional
    public int deleteByStatusBulk(LeadStatus status) {
        int deleted = repository.deleteByStatusBulk(status);
        LOG.info("Bulk delete: removed {} leads with status {}", deleted, status);
        return deleted;
    }

    // ===== СЛОЖНЫЙ ПОИСК =====

    public List<Lead> searchLeads(String name, String email, String company, LeadStatus status) {
        List<Lead> results;

        if (status != null) {
            results = repository.findByStatus(status);
        } else {
            results = repository.findAll();
        }

        if (name != null && !name.isBlank()) {
            results = results.stream()
                    .filter(lead -> lead.name() != null &&
                            lead.name().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }

        if (email != null && !email.isBlank()) {
            results = results.stream()
                    .filter(lead -> lead.email().toLowerCase().contains(email.toLowerCase()))
                    .toList();
        }

        if (company != null && !company.isBlank()) {
            results = results.stream()
                    .filter(lead -> lead.company().toLowerCase().contains(company.toLowerCase()))
                    .toList();
        }

        LOG.debug("Search leads found {} results", results.size());
        return results;
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ ПРИВАТНЫЕ МЕТОДЫ =====

    /**
     * Проверяет, что email уникален.
     * @throws IllegalStateException если email уже существует
     */
    private void checkEmailUniqueness(String email) {
        if (repository.existsByEmail(email)) {
            throw new IllegalStateException("Lead with email already exists: " + email);
        }
    }

    /**
     * Находит лида по ID или бросает исключение.
     * @throws ResponseStatusException если лид не найден
     */
    private Lead findLeadByIdOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cannot find lead with id " + id
                ));
    }
}