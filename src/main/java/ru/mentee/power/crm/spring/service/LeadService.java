package ru.mentee.power.crm.spring.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Deal;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.DealRepository;
import ru.mentee.power.crm.repository.LeadRepository;

@Service
public class LeadService {
    private static final Logger LOG = LoggerFactory.getLogger(LeadService.class);
    private final LeadRepository leadRepository;
    private final DealRepository dealRepository;
    private final LeadProcessor leadProcessor;

    public LeadService(LeadRepository leadRepository,
                       DealRepository dealRepository,
                       LeadProcessor leadProcessor) {
        this.leadRepository = leadRepository;
        this.dealRepository = dealRepository;
        this.leadProcessor = leadProcessor;
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
        Lead saved = leadRepository.save(lead);
        LOG.info("Created lead with id: {}, email: {}", saved.id(), saved.email());
        return saved;
    }

    @Transactional
    public Lead addLead(String email, String company, LeadStatus status) {
        checkEmailUniqueness(email);
        Lead lead = new Lead(email, company, status);
        Lead saved = leadRepository.save(lead);
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

        Lead saved = leadRepository.save(existing);
        LOG.info("Updated lead with id: {}", id);
        return saved;
    }

    // ===== DELETE (удаление) =====

    @Transactional
    public void delete(UUID id) {
        findLeadByIdOrThrow(id);
        leadRepository.deleteById(id);
        LOG.info("Deleted lead with id: {}", id);
    }

    // ===== READ (чтение) — простые методы =====

    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    public Optional<Lead> findById(UUID id) {
        return leadRepository.findById(id);
    }

    public Optional<Lead> findByEmail(String email) {
        return leadRepository.findByEmail(email);
    }

    // ===== READ — улучшенные методы =====

    public List<Lead> findByStatus(LeadStatus status) {
        return leadRepository.findByStatus(status);
    }

    public List<Lead> findByCompany(String company) {
        return leadRepository.findByCompany(company);
    }

    public List<Lead> findByEmailContaining(String emailPart) {
        if (emailPart == null || emailPart.isBlank()) {
            return findAll();
        }
        return leadRepository.findByEmailContaining(emailPart);
    }

    public List<Lead> findByStatusAndCompany(LeadStatus status, String company) {
        return leadRepository.findByStatusAndCompany(status, company);
    }

    public long countByStatus(LeadStatus status) {
        return leadRepository.countByStatus(status);
    }

    public boolean existsByEmail(String email) {
        return leadRepository.existsByEmail(email);
    }

    public List<Lead> findByStatuses(LeadStatus... statuses) {
        return  leadRepository.findByStatusIn(List.of(statuses));
    }


    public List<Lead> findCreatedAfter(LocalDateTime date) {
        if (date == null) {
            return findAll();
        }
        return leadRepository.findCreatedAfter(date);
    }

    public List<Lead> findByCompanyOrderedByDate(String company) {
        return leadRepository.findByCompanyOrderedByDate(company);
    }

    // ===== ПАГИНАЦИЯ =====

    public Page<Lead> findAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return leadRepository.findAll(pageable);
    }

    public Page<Lead> findByStatusPaged(LeadStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return leadRepository.findByStatus(status, pageable);
    }

    public Page<Lead> findByCompanyPaged(String company, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return leadRepository.findByCompany(company, pageable);
    }

    // ===== BULK ОПЕРАЦИИ =====

    @Transactional
    public int convertNewToContacted() {
        int updated = leadRepository.updateStatusBulk(LeadStatus.NEW, LeadStatus.CONTACTED);
        LOG.info("Bulk update: converted {} leads from NEW to CONTACTED", updated);
        return updated;
    }

    @Transactional
    public int deleteByStatusBulk(LeadStatus status) {
        int deleted = leadRepository.deleteByStatusBulk(status);
        LOG.info("Bulk delete: removed {} leads with status {}", deleted, status);
        return deleted;
    }

    // ===== СЛОЖНЫЙ ПОИСК =====

    public List<Lead> searchLeads(String name, String email, String company, LeadStatus status) {
        List<Lead> results;

        if (status != null) {
            results = leadRepository.findByStatus(status);
        } else {
            results = leadRepository.findAll();
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
        if (leadRepository.existsByEmail(email)) {
            throw new IllegalStateException("Lead with email already exists: " + email);
        }
    }

    /**
     * Находит лида по ID или бросает исключение.
     * @throws ResponseStatusException если лид не найден
     */
    private Lead findLeadByIdOrThrow(UUID id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cannot find lead with id " + id
                ));
    }

    @Transactional
    public void convertLeadToDeal (UUID leadId, BigDecimal amount) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lead not found: " + leadId));
        Deal deal = new Deal(leadId, amount);
        lead.setStatus(LeadStatus.CONTACTED);
        dealRepository.save(deal);
    }

    public String processLeads(List<UUID> ids) {
        String transactionName = "None";
        for (UUID id : ids) {
            try {
                transactionName = leadProcessor.processSingleLead(id);
            } catch (Exception e) {
                // Перехват исключения
                System.out.println("Failed to process lead: " + id);
            }
        }
        return transactionName;
    }

    //self-invocation
    public void processLeadsWithInvocationProblem(List<UUID> ids) {
        for (UUID id : ids) {
            try {
                this.processSingleLead(id);
            } catch (Exception e) {
                // Перехват исключения
                System.out.println("Failed to process lead: " + id);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void processSingleLead(UUID id) {
        if (leadRepository.existsById(id)) {
            leadRepository.findById(id).get().setStatus(LeadStatus.CONTACTED);
        } else {
            throw new IllegalArgumentException(); //ошибка для rollback
        }
    }

    public String processLeadsWithRequires(List<UUID> ids) {
        String transactionName = "none";
        for (UUID id : ids) {
            try {
                transactionName = leadProcessor.processSingleLeadWithRequired(id);
            } catch (Exception e) {
                // Перехват исключения
                System.out.println("Failed to process lead: " + id);
            }
        }

        return transactionName;
    }

    public String processLeadsWithMandatory(List<UUID> ids) {
        String transactionName = "none";
        for (UUID id : ids) {
            try {
                transactionName = leadProcessor.processSingleLeadWithMandatory(id);
            } catch (IllegalArgumentException e) {
                // Перехват исключения
                System.out.println("Failed to process lead: " + id);
            }
        }

        return transactionName;
    }

    // Транзакция A (читает) для последовательного вызова
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<String> readThenWriteThenReadAgainWithReadCommitted(UUID leadId, String newName) {
        List<String> results = new ArrayList<>();

        //Транзакция A читает Lead (name = "John")
        Lead lead = leadRepository.findById(leadId).orElseThrow();
        results.add(lead.getName());  // "John"

        //Транзакция B обновляет Lead (name = "Jane") и commit
        updateLeadName(leadId, newName);  // обновляет на "Jane"

        // Транзакция A читает Lead повторно
        // должны увидеть "Jane" при READ_COMMITTED
        lead = leadRepository.findById(leadId).orElseThrow();
        results.add(lead.getName());

        return results;
    }

    // Транзакция B (обновляет)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateLeadName(UUID leadId, String newName) {
        Lead lead = leadRepository.findById(leadId).orElseThrow();
        lead.setName(newName);
        // Транзакция Б завершается и КОММИТИТ
    }

    // Метод для REPEATABLE_READ в параллельном тесте
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<String> readLeadNameWithRepeatableRead(UUID leadId) throws InterruptedException {
        List<String> results = new ArrayList<>();

        Lead lead = leadRepository.findById(leadId).orElseThrow();
        results.add(lead.getName());

        Thread.sleep(100);

        lead = leadRepository.findById(leadId).orElseThrow();
        results.add(lead.getName());

        return results;
    }

    public Page<Lead> getFirstPage(int pageSize) {
        PageRequest pageRequest = PageRequest.of(
                0, pageSize, Sort.by("createdAt").descending());
        return leadRepository.findAll(pageRequest);
    }

    public Page<Lead> searchByCompany (String company, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber, pageSize);
        return leadRepository.findByCompany(company, pageRequest);
    }
}


