package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadRepositoryTest {

    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        // Создаем новую реализацию репозитория перед каждым тестом
        repository = new InMemoryLeadRepository();
    }

    @Test
    void shouldSaveLead() {
        // Given
        Lead lead = new Lead(
                UUID.randomUUID(),
                "test@example.com",
                "Test Company",
                LeadStatus.NEW
        );

        // When
        Lead saved = repository.save(lead);

        // Then
        assertThat(saved).isEqualTo(lead);
        assertThat(repository.findById(lead.id())).isPresent();
    }

    @Test
    void shouldFindLeadById() {
        // Given
        Lead lead = new Lead(
                UUID.randomUUID(),
                "find@example.com",
                "Find Company",
                LeadStatus.NEW
        );
        repository.save(lead);

        // When
        Optional<Lead> found = repository.findById(lead.id());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(lead);
        assertThat(found.get().email()).isEqualTo("find@example.com");
    }

    @Test
    void shouldReturnEmptyOptionalWhenIdNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        Optional<Lead> found = repository.findById(nonExistentId);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindLeadByEmail() {
        // Given
        Lead lead = new Lead(
                UUID.randomUUID(),
                "email@example.com",
                "Email Company",
                LeadStatus.NEW
        );
        repository.save(lead);

        // When
        Optional<Lead> found = repository.findByEmail("email@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(lead);
        assertThat(found.get().company()).isEqualTo("Email Company");
    }

    @Test
    void shouldReturnEmptyOptionalWhenEmailNotFound() {
        // Given
        String nonExistentEmail = "nonexistent@example.com";

        // When
        Optional<Lead> found = repository.findByEmail(nonExistentEmail);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAllLeads() {
        // Given
        Lead lead1 = new Lead(
                UUID.randomUUID(),
                "first@example.com",
                "First Company",
                LeadStatus.NEW
        );

        Lead lead2 = new Lead(
                UUID.randomUUID(),
                "second@example.com",
                "Second Company",
                LeadStatus.QUALIFIED
        );

        repository.save(lead1);
        repository.save(lead2);

        // When
        List<Lead> allLeads = repository.findAll();

        // Then
        assertThat(allLeads).hasSize(2);
        assertThat(allLeads).containsExactlyInAnyOrder(lead1, lead2);
    }

    @Test
    void shouldReturnEmptyListWhenNoLeads() {
        // When
        List<Lead> allLeads = repository.findAll();

        // Then
        assertThat(allLeads).isEmpty();
    }

    @Test
    void shouldDeleteLead() {
        // Given
        Lead lead = new Lead(
                UUID.randomUUID(),
                "delete@example.com",
                "Delete Company",
                LeadStatus.NEW
        );
        repository.save(lead);

        // When
        repository.delete(lead.id());

        // Then
        assertThat(repository.findById(lead.id())).isEmpty();
        assertThat(repository.findByEmail("delete@example.com")).isEmpty();
    }

    @Test
    void shouldNotThrowErrorWhenDeletingNonExistentId() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When/Then - should not throw any exception
        assertThatCode(() -> repository.delete(nonExistentId))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldUpdateLeadWhenSavingWithSameId() {
        // Given
        UUID id = UUID.randomUUID();
        Lead originalLead = new Lead(
                id,
                "original@example.com",
                "Original Company",
                LeadStatus.NEW
        );
        repository.save(originalLead);

        // When - сохраняем лида с тем же ID, но другими данными
        Lead updatedLead = new Lead(
                id,
                "original@example.com",  // email тот же
                "Updated Company",        // компания изменилась
                LeadStatus.QUALIFIED      // статус изменился
        );
        repository.save(updatedLead);

        // Then
        Optional<Lead> found = repository.findById(id);
        assertThat(found).isPresent();
        assertThat(found.get().company()).isEqualTo("Updated Company");
        assertThat(found.get().status()).isEqualTo(LeadStatus.QUALIFIED);

        // Проверяем, что в репозитории только один лид
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void shouldMaintainEmailIndexAfterDelete() {
        // Given
        Lead lead = new Lead(
                UUID.randomUUID(),
                "index@example.com",
                "Index Company",
                LeadStatus.NEW
        );
        repository.save(lead);

        // When
        repository.delete(lead.id());

        // Then - email индекс тоже должен очиститься
        assertThat(repository.findByEmail("index@example.com")).isEmpty();
    }

    @Test
    void shouldHandleMultipleLeadsWithDifferentEmails() {
        // Given
        Lead lead1 = new Lead(
                UUID.randomUUID(),
                "multi1@example.com",
                "Company 1",
                LeadStatus.NEW
        );

        Lead lead2 = new Lead(
                UUID.randomUUID(),
                "multi2@example.com",
                "Company 2",
                LeadStatus.QUALIFIED
        );

        Lead lead3 = new Lead(
                UUID.randomUUID(),
                "multi3@example.com",
                "Company 3",
                LeadStatus.CONTACTED
        );

        // When
        repository.save(lead1);
        repository.save(lead2);
        repository.save(lead3);

        // Then
        assertThat(repository.findAll()).hasSize(3);
        assertThat(repository.findByEmail("multi1@example.com")).isPresent();
        assertThat(repository.findByEmail("multi2@example.com")).isPresent();
        assertThat(repository.findByEmail("multi3@example.com")).isPresent();
    }
}
