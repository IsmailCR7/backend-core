package ru.mentee.power.crm.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class LeadServiceTest {

    @Autowired
    private LeadService service;

    @Autowired
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        // Создаём тестовые данные
        for (int i = 1; i <= 3; i++) {
            Lead lead = new Lead();
            lead.setName("Lead " + i);
            lead.setEmail("lead" + i + "@example.com");
            lead.setCompany("Company " + i);
            lead.setStatus(LeadStatus.NEW);
            lead.setCreatedAt(LocalDateTime.now());
            repository.save(lead);
        }

        // Добавляем один CONTACTED лид
        Lead contactedLead = new Lead();
        contactedLead.setName("Contacted Lead");
        contactedLead.setEmail("contacted@example.com");
        contactedLead.setCompany("Contacted Corp");
        contactedLead.setStatus(LeadStatus.CONTACTED);
        contactedLead.setCreatedAt(LocalDateTime.now());
        repository.save(contactedLead);
    }

    // ===== ТЕСТЫ CRUD ОПЕРАЦИЙ =====

    @Test
    void addLeadShouldCreateNewLead() {
        // When
        Lead newLead = service.addLead("Test User", "test@example.com", "Test Corp", LeadStatus.NEW);

        // Then
        assertThat(newLead).isNotNull();
        assertThat(newLead.id()).isNotNull();
        assertThat(newLead.email()).isEqualTo("test@example.com");

        Optional<Lead> found = repository.findByEmail("test@example.com");
        assertThat(found).isPresent();
    }

    @Test
    void addLeadShouldThrowExceptionWhenEmailAlreadyExists() {
        // When & Then
        assertThatThrownBy(() -> service.addLead("Duplicate", "lead1@example.com", "Corp", LeadStatus.NEW))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Lead with email already exists");
    }

    @Test
    void updateShouldModifyExistingLead() {
        // Given
        Lead existing = repository.findByEmail("lead1@example.com").get();

        // When
        Lead updatedLead = new Lead();
        updatedLead.setName("Updated Name");
        updatedLead.setEmail("updated@example.com");
        updatedLead.setCompany("Updated Corp");
        updatedLead.setStatus(LeadStatus.CONTACTED);

        Lead result = service.update(existing.id(), updatedLead);

        // Then
        assertThat(result.name()).isEqualTo("Updated Name");
        assertThat(result.email()).isEqualTo("updated@example.com");

        Optional<Lead> oldEmail = repository.findByEmail("lead1@example.com");
        assertThat(oldEmail).isEmpty();

        Optional<Lead> newEmail = repository.findByEmail("updated@example.com");
        assertThat(newEmail).isPresent();
    }

    @Test
    void deleteShouldRemoveLead() {
        // Given
        Lead toDelete = repository.findByEmail("lead1@example.com").get();

        // When
        service.delete(toDelete.id());

        // Then
        Optional<Lead> found = repository.findByEmail("lead1@example.com");
        assertThat(found).isEmpty();
    }

    // ===== ТЕСТЫ МЕТОДОВ ПОИСКА =====

    @Test
    void findByEmailShouldReturnLead() {
        // When
        Optional<Lead> found = service.findByEmail("lead2@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo("Company 2");
    }

    @Test
    void findByStatusShouldReturnCorrectLeads() {
        // When
        List<Lead> newLeads = service.findByStatus(LeadStatus.NEW);
        List<Lead> contactedLeads = service.findByStatus(LeadStatus.CONTACTED);

        // Then
        assertThat(newLeads).hasSize(3);
        assertThat(contactedLeads).hasSize(1);
    }

    @Test
    void findByCompanyShouldReturnLeads() {
        // When
        List<Lead> companyLeads = service.findByCompany("Company 1");

        // Then
        assertThat(companyLeads).hasSize(1);
        assertThat(companyLeads.get(0).getEmail()).isEqualTo("lead1@example.com");
    }

    @Test
    void countByStatusShouldReturnCorrectCount() {
        // When
        long newCount = service.countByStatus(LeadStatus.NEW);
        long contactedCount = service.countByStatus(LeadStatus.CONTACTED);

        // Then
        assertThat(newCount).isEqualTo(3);
        assertThat(contactedCount).isEqualTo(1);
    }

    @Test
    void existsByEmailShouldReturnCorrectResult() {
        // When
        boolean exists = service.existsByEmail("lead1@example.com");
        boolean notExists = service.existsByEmail("fake@example.com");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void findByEmailContainingShouldReturnMatchingLeads() {
        // When
        List<Lead> found = service.findByEmailContaining("lead");

        // Then
        assertThat(found).hasSize(3);
    }

    @Test
    void findByStatusAndCompanyShouldReturnCorrectLead() {
        // When
        List<Lead> found = service.findByStatusAndCompany(LeadStatus.NEW, "Company 1");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getEmail()).isEqualTo("lead1@example.com");
    }

    // ===== ТЕСТЫ ПАГИНАЦИИ =====

    @Test
    void findAllPagedShouldReturnPage() {
        // When
        Page<Lead> page = service.findAllPaged(0, 2);

        // Then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void findByCompanyPagedShouldReturnPagedResults() {
        // Create more ACME leads
        for (int i = 1; i <= 3; i++) {
            Lead lead = new Lead();
            lead.setName("ACME Lead " + i);
            lead.setEmail("acme" + i + "@acme.com");
            lead.setCompany("ACME Corp");
            lead.setStatus(LeadStatus.NEW);
            repository.save(lead);
        }

        // When
        Page<Lead> page = service.findByCompanyPaged("ACME Corp", 0, 2);

        // Then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    // ===== ТЕСТЫ BULK ОПЕРАЦИЙ =====

    @Test
    void convertNewToContactedShouldUpdateAllNewLeads() {
        // When
        int updated = service.convertNewToContacted();

        // Then
        assertThat(updated).isEqualTo(3);

        long contactedCount = service.countByStatus(LeadStatus.CONTACTED);
        assertThat(contactedCount).isEqualTo(4); // 3 обновлённых + 1 существующий

        long newCount = service.countByStatus(LeadStatus.NEW);
        assertThat(newCount).isEqualTo(0);
    }

    @Test
    void deleteByStatusBulkShouldDeleteAllLeadsWithStatus() {
        // When
        int deleted = service.deleteByStatusBulk(LeadStatus.NEW);

        // Then
        assertThat(deleted).isEqualTo(3);

        long remaining = repository.count();
        assertThat(remaining).isEqualTo(1); // Только contacted лид остался

        Optional<Lead> contactedLead = repository.findByEmail("contacted@example.com");
        assertThat(contactedLead).isPresent();
    }

    // ===== ТЕСТ ПОИСКА С ФИЛЬТРАМИ =====

    @Test
    void searchLeadsShouldFilterByName() {
        // When
        List<Lead> results = service.searchLeads("Lead 1", null, null, null);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("lead1@example.com");
    }

    @Test
    void searchLeadsShouldFilterByStatus() {
        // When
        List<Lead> results = service.searchLeads(null, null, null, LeadStatus.NEW);

        // Then
        assertThat(results).hasSize(3);
    }

    @Test
    void searchLeadsShouldCombineFilters() {
        // When
        List<Lead> results = service.searchLeads("Lead", null, "Company 1", LeadStatus.NEW);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("lead1@example.com");
    }
}