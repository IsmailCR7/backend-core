package ru.mentee.power.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class LeadServiceTest {

    private LeadService service;
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLeadRepository();
        service = new LeadService(repository);
    }

    @Test
    void shouldCreateLeadWhenEmailIsUnique() {
        String email = "test@example.com";
        String company = "Test Company";
        LeadStatus status = LeadStatus.NEW;

        Lead result = service.addLead(email, company, status);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(email);
        assertThat(result.company()).isEqualTo(company);
        assertThat(result.status()).isEqualTo(status);
        assertThat(result.id()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "duplicate@example.com";
        service.addLead(email, "First Company", LeadStatus.NEW);

        assertThatThrownBy(() ->
                service.addLead(email, "Second Company", LeadStatus.NEW)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Lead with email already exists");
    }

    @Test
    void shouldFindAllLeads() {
        service.addLead("one@example.com", "Company 1", LeadStatus.NEW);
        service.addLead("two@example.com", "Company 2", LeadStatus.CONTACTED);

        List<Lead> result = service.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindLeadById() {
        Lead created = service.addLead("find@example.com", "Company", LeadStatus.NEW);

        Optional<Lead> result = service.findById(created.id());

        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("find@example.com");
    }

    @Test
    void shouldFindLeadByEmail() {
        service.addLead("search@example.com", "Company", LeadStatus.NEW);

        Optional<Lead> result = service.findByEmail("search@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().company()).isEqualTo("Company");
    }

    @Test
    void shouldReturnEmptyWhenLeadNotFound() {
        Optional<Lead> result = service.findByEmail("nonexistent@example.com");

        assertThat(result).isEmpty();
    }
    @Test
    void shouldReturnOnlyNewLeadsWhenFindByStatusNew() {
        service.addLead("new1@test.com", "New Company 1", LeadStatus.NEW);
        service.addLead("new2@test.com", "New Company 2", LeadStatus.NEW);
        service.addLead("new3@test.com", "New Company 3", LeadStatus.NEW);

        service.addLead("contacted1@test.com", "Contacted Company 1", LeadStatus.CONTACTED);
        service.addLead("contacted2@test.com", "Contacted Company 2", LeadStatus.CONTACTED);
        service.addLead("contacted3@test.com", "Contacted Company 3", LeadStatus.CONTACTED);
        service.addLead("contacted4@test.com", "Contacted Company 4", LeadStatus.CONTACTED);
        service.addLead("contacted5@test.com", "Contacted Company 5", LeadStatus.CONTACTED);

        service.addLead("qualified1@test.com", "Qualified Company 1", LeadStatus.QUALIFIED);
        service.addLead("qualified2@test.com", "Qualified Company 2", LeadStatus.QUALIFIED);

        List<Lead> result = service.findByStatus(LeadStatus.NEW);

        assertThat(result).hasSize(3);
        assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.NEW));
        assertThat(result).extracting(Lead ::email)
                .containsExactlyInAnyOrder("new1@test.com", "new2@test.com", "new3@test.com");
    }
    @Test
    void shouldReturnOnlyQualifiedLeadsWhenFindByStatusQualified() {
        service.addLead("new@test.com", "New Company", LeadStatus.NEW);
        service.addLead("contacted@test.com", "Contacted Company", LeadStatus.CONTACTED);

        service.addLead("qualified1@test.com", "Qualified Company 1", LeadStatus.QUALIFIED);
        service.addLead("qualified2@test.com", "Qualified Company 2", LeadStatus.QUALIFIED);

        List<Lead> result = service.findByStatus(LeadStatus.QUALIFIED);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.QUALIFIED));
        assertThat(result).extracting(Lead ::email)
                .containsExactlyInAnyOrder("qualified1@test.com", "qualified2@test.com");

    }
    @Test
    void shouldReturnOnlyContactedLeadsWhenFindByStatusContacted() {
        service.addLead("new@test.com", "New Company", LeadStatus.NEW);

        service.addLead("contacted1@test.com", "Contacted Company 1", LeadStatus.CONTACTED);
        service.addLead("contacted2@test.com", "Contacted Company 2", LeadStatus.CONTACTED);
        service.addLead("contacted3@test.com", "Contacted Company 3", LeadStatus.CONTACTED);

        service.addLead("qualified@test.com", "Qualified Company", LeadStatus.QUALIFIED);

        List<Lead> result = service.findByStatus(LeadStatus.CONTACTED);

        assertThat(result).hasSize(3);
        assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.CONTACTED));
        assertThat(result).extracting(Lead::email)
                .containsExactlyInAnyOrder("contacted1@test.com", "contacted2@test.com", "contacted3@test.com");
    }

    @Test
    void shouldReturnEmptyListWhenNoLeadsWithStatus() {
        service.addLead("new1@test.com", "New Company 1", LeadStatus.NEW);
        service.addLead("new2@test.com", "New Company 2", LeadStatus.NEW);
        service.addLead("contacted1@test.com", "Contacted Company 1", LeadStatus.CONTACTED);
        service.addLead("contacted2@test.com", "Contacted Company 2", LeadStatus.CONTACTED);

        List<Lead> result = service.findByStatus(LeadStatus.QUALIFIED);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryIsEmpty() {
        List<Lead> resultForNew = service.findByStatus(LeadStatus.NEW);
        List<Lead> resultForContacted = service.findByStatus(LeadStatus.CONTACTED);
        List<Lead> resultForQualified = service.findByStatus(LeadStatus.QUALIFIED);

        assertThat(resultForNew).isEmpty();
        assertThat(resultForContacted).isEmpty();
        assertThat(resultForQualified).isEmpty();
    }
    @ParameterizedTest
            @CsvSource({
            "NEW, 3",
            "CONTACTED, 5",
            "QUALIFIED, 2"
    })
    void shouldReturnCorrectCountForEachStatus(LeadStatus status, int expectedCount) {
        // Given
        service.addLead("new1@test.com", "New Company 1", LeadStatus.NEW);
        service.addLead("new2@test.com", "New Company 2", LeadStatus.NEW);
        service.addLead("new3@test.com", "New Company 3", LeadStatus.NEW);

        service.addLead("contacted1@test.com", "Contacted Company 1", LeadStatus.CONTACTED);
        service.addLead("contacted2@test.com", "Contacted Company 2", LeadStatus.CONTACTED);
        service.addLead("contacted3@test.com", "Contacted Company 3", LeadStatus.CONTACTED);
        service.addLead("contacted4@test.com", "Contacted Company 4", LeadStatus.CONTACTED);
        service.addLead("contacted5@test.com", "Contacted Company 5", LeadStatus.CONTACTED);

        service.addLead("qualified1@test.com", "Qualified Company 1", LeadStatus.QUALIFIED);
        service.addLead("qualified2@test.com", "Qualified Company 2", LeadStatus.QUALIFIED);

        List<Lead> result = service.findByStatus(status);

        assertThat(result).hasSize(expectedCount);
        assertThat(result).allMatch(lead -> lead.status().equals(status));
    }
}