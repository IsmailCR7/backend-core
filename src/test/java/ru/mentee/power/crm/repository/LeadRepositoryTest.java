package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.Application;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = Application.class)  // ← Указываем главный конфигурационный класс
class LeadRepositoryTest {

    @Autowired
    private LeadRepository repository;

    private Lead testLead;

    @BeforeEach
    void setUp() {
        testLead = new Lead("test@example.com", "Test Company", LeadStatus.NEW);
        repository.deleteAll();  // Очищаем БД перед каждым тестом
    }

    @Test
    void shouldSaveLead() {
        // When
        Lead saved = repository.save(testLead);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldFindByEmailDerivedQuery() {
        // Given
        repository.save(testLead);

        // When
        Optional<Lead> found = repository.findByEmail("test@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo("Test Company");
    }

    @Test
    void shouldFindByEmailNativeQuery() {
        // Given
        repository.save(testLead);

        // When
        Optional<Lead> found = repository.findByEmailNative("test@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo("Test Company");
    }

    @Test
    void shouldFindByStatusDerivedQuery() {
        // Given
        repository.save(testLead);
        repository.save(new Lead("new2@test.com", "Another Co", LeadStatus.NEW));
        repository.save(new Lead("contacted@test.com", "Contacted Co", LeadStatus.CONTACTED));

        // When
        List<Lead> newLeads = repository.findByStatus(LeadStatus.NEW);

        // Then
        assertThat(newLeads).hasSize(2);
        assertThat(newLeads).allMatch(lead -> lead.getStatus() == LeadStatus.NEW);
    }

    @Test
    void shouldFindByStatusNativeQuery() {
        // Given
        repository.save(testLead);
        repository.save(new Lead("qualified@test.com", "Qualified Co", LeadStatus.QUALIFIED));

        // When
        List<Lead> qualifiedLeads = repository.findByStatusNative("QUALIFIED");

        // Then
        assertThat(qualifiedLeads).hasSize(1);
        assertThat(qualifiedLeads.get(0).getEmail()).isEqualTo("qualified@test.com");
    }

    @Test
    void shouldCountByStatusNative() {
        // Given
        repository.save(testLead);
        repository.save(new Lead("new2@test.com", "Another Co", LeadStatus.NEW));
        repository.save(new Lead("contacted@test.com", "Contacted Co", LeadStatus.CONTACTED));

        // When
        long newCount = repository.countByStatusNative("NEW");
        long contactedCount = repository.countByStatusNative("CONTACTED");

        // Then
        assertThat(newCount).isEqualTo(2);
        assertThat(contactedCount).isEqualTo(1);
    }

    @Test
    void shouldUpdateLead() {
        // Given
        Lead saved = repository.save(testLead);

        // When
        saved.setCompany("Updated Company");
        saved.setStatus(LeadStatus.QUALIFIED);
        Lead updated = repository.save(saved);

        // Then
        Optional<Lead> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getCompany()).isEqualTo("Updated Company");
        assertThat(found.get().getStatus()).isEqualTo(LeadStatus.QUALIFIED);
    }

    @Test
    void shouldDeleteLead() {
        // Given
        Lead saved = repository.save(testLead);

        // When
        repository.deleteById(saved.getId());

        // Then
        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldReturnEmptyOptionalWhenEmailNotFound() {
        // When
        Optional<Lead> found = repository.findByEmail("nonexistent@test.com");

        // Then
        assertThat(found).isEmpty();
    }
}