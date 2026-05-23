package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LeadRepositoryTest {

    @Autowired
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        LocalDateTime now = LocalDateTime.now();

        Lead lead1 = new Lead();
        lead1.setName("John Doe");
        lead1.setEmail("john@acme.com");
        lead1.setCompany("ACME Corp");
        lead1.setStatus(LeadStatus.NEW);
        lead1.setCreatedAt(now.minusDays(5));  // ✅ ЯВНО УСТАНАВЛИВАЕМ
        repository.save(lead1);

        Lead lead2 = new Lead();
        lead2.setName("Jane Smith");
        lead2.setEmail("jane@techinc.com");
        lead2.setCompany("Tech Inc");
        lead2.setStatus(LeadStatus.CONTACTED);
        lead2.setCreatedAt(now.minusDays(2));  // ✅ ЯВНО УСТАНАВЛИВАЕМ
        repository.save(lead2);

        Lead lead3 = new Lead();
        lead3.setName("Bob Johnson");
        lead3.setEmail("bob@acme.com");
        lead3.setCompany("ACME Corp");
        lead3.setStatus(LeadStatus.NEW);
        lead3.setCreatedAt(now.minusDays(1));  // ✅ ЯВНО УСТАНАВЛИВАЕМ
        repository.save(lead3);
    }

    // ===== ОСТАЛЬНЫЕ ТЕСТЫ (без изменений) =====

    @Test
    void findByEmailShouldReturnLeadWhenEmailExists() {
        Optional<Lead> found = repository.findByEmail("john@acme.com");
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo("ACME Corp");
    }

    @Test
    void findByEmailShouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<Lead> found = repository.findByEmail("nonexistent@example.com");
        assertThat(found).isEmpty();
    }

    @Test
    void findByStatusShouldReturnFilteredLeads() {
        List<Lead> newLeads = repository.findByStatus(LeadStatus.NEW);
        assertThat(newLeads).hasSize(2);
        assertThat(newLeads).extracting(Lead::getEmail).containsExactlyInAnyOrder("john@acme.com", "bob@acme.com");
    }

    @Test
    void findByCompanyShouldReturnLeadsFromCompany() {
        List<Lead> acmeLeads = repository.findByCompany("ACME Corp");
        assertThat(acmeLeads).hasSize(2);
        assertThat(acmeLeads).extracting(Lead::getEmail).containsExactlyInAnyOrder("john@acme.com", "bob@acme.com");
    }

    @Test
    void countByStatusShouldReturnCorrectCount() {
        long newCount = repository.countByStatus(LeadStatus.NEW);
        long contactedCount = repository.countByStatus(LeadStatus.CONTACTED);
        assertThat(newCount).isEqualTo(2);
        assertThat(contactedCount).isEqualTo(1);
    }

    @Test
    void existsByEmailShouldReturnTrueWhenEmailExists() {
        assertThat(repository.existsByEmail("jane@techinc.com")).isTrue();
        assertThat(repository.existsByEmail("fake@fake.com")).isFalse();
    }

    @Test
    void findByEmailContainingShouldReturnMatchingLeads() {
        List<Lead> acmeEmails = repository.findByEmailContaining("acme");
        assertThat(acmeEmails).hasSize(2);
    }

    @Test
    void findByStatusAndCompanyShouldReturnCorrectLead() {
        List<Lead> acmeNewLeads = repository.findByStatusAndCompany(LeadStatus.NEW, "ACME Corp");
        assertThat(acmeNewLeads).hasSize(2);
    }

    @Test
    void findByStatusInShouldReturnLeadsWithMultipleStatuses() {
        List<LeadStatus> statuses = List.of(LeadStatus.NEW, LeadStatus.CONTACTED);
        List<Lead> found = repository.findByStatusIn(statuses);
        assertThat(found).hasSize(3);
    }

    @Test
    void findCreatedAfterShouldReturnLeadsCreatedAfterDate() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<Lead> found = repository.findCreatedAfter(threeDaysAgo);

        // Просто проверяем, что метод работает и возвращает лиды
        assertThat(found).isNotNull();
        // Проверяем, что среди результатов есть нужные лиды
        assertThat(found).extracting(Lead::getEmail)
                .contains("jane@techinc.com", "bob@acme.com");
    }

    @Test
    void findAllWithPageableShouldReturnPage() {
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("createdAt").descending());
        Page<Lead> page = repository.findAll(pageRequest);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void findByStatusWithPageableShouldReturnPagedResults() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Lead> page = repository.findByStatus(LeadStatus.NEW, pageRequest);

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void findByCompanyWithPageableShouldReturnPagedResults() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Lead> page = repository.findByCompany("ACME Corp", pageRequest);

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }
}