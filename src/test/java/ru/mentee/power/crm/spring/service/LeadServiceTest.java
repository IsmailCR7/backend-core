package ru.mentee.power.crm.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(LeadService.class)  // ⚠️ ВАЖНО: импортируем сервис!
class LeadServiceTest {

    @Autowired
    private LeadService leadService;

    @Autowired
    private LeadRepository leadRepository;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        leadRepository.deleteAll();
    }

    @Test
    void shouldCreateLeadWhenEmailIsUnique() {
        Lead result = leadService.addLead("test@example.com", "Test Company", LeadStatus.NEW);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        leadService.addLead("duplicate@example.com", "First Company", LeadStatus.NEW);

        assertThatThrownBy(() ->
                leadService.addLead("duplicate@example.com", "Second Company", LeadStatus.NEW)
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldFindLeadByEmail() {
        leadService.addLead("search@example.com", "Company", LeadStatus.NEW);

        Optional<Lead> result = leadService.findByEmail("search@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getCompany()).isEqualTo("Company");
    }

    @Test
    void shouldFindAllLeads() {
        leadService.addLead("one@example.com", "Company 1", LeadStatus.NEW);
        leadService.addLead("two@example.com", "Company 2", LeadStatus.CONTACTED);

        List<Lead> result = leadService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindLeadById() {
        Lead created = leadService.addLead("find@example.com", "Company", LeadStatus.NEW);

        Optional<Lead> result = leadService.findById(created.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("find@example.com");
    }

    @Test
    void shouldReturnEmptyWhenLeadNotFound() {
        Optional<Lead> result = leadService.findByEmail("nonexistent@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOnlyNewLeadsWhenFindByStatusNew() {
        leadService.addLead("new1@test.com", "New Company 1", LeadStatus.NEW);
        leadService.addLead("new2@test.com", "New Company 2", LeadStatus.NEW);
        leadService.addLead("contacted1@test.com", "Contacted Company", LeadStatus.CONTACTED);

        List<Lead> result = leadService.findByStatus(LeadStatus.NEW);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(lead -> lead.getStatus() == LeadStatus.NEW);
    }

    @Test
    void shouldReturnOnlyContactedLeadsWhenFindByStatusContacted() {
        leadService.addLead("new@test.com", "New Company", LeadStatus.NEW);
        leadService.addLead("contacted1@test.com", "Contacted Company 1", LeadStatus.CONTACTED);
        leadService.addLead("contacted2@test.com", "Contacted Company 2", LeadStatus.CONTACTED);

        List<Lead> result = leadService.findByStatus(LeadStatus.CONTACTED);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(lead -> lead.getStatus() == LeadStatus.CONTACTED);
    }

    @Test
    void shouldReturnEmptyListWhenNoLeadsWithStatus() {
        leadService.addLead("new1@test.com", "New Company", LeadStatus.NEW);

        List<Lead> result = leadService.findByStatus(LeadStatus.QUALIFIED);

        assertThat(result).isEmpty();
    }
    @Test
    void shouldSearchByNameOrEmailWhenSearchTextMatchesEmail() {
        // Given
        leadService.addLead("john.doe@example.com", "Tech Corp", LeadStatus.NEW);
        leadService.addLead("jane.smith@example.com", "Biz Inc", LeadStatus.CONTACTED);
        leadService.addLead("bob.wilson@example.com", "Startup LLC", LeadStatus.QUALIFIED);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("john.doe", null);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void shouldSearchByNameOrEmailWhenSearchTextMatchesCompany() {
        // Given
        leadService.addLead("john@tech.com", "Tech Corp", LeadStatus.NEW);
        leadService.addLead("jane@biz.com", "Biz Inc", LeadStatus.CONTACTED);
        leadService.addLead("bob@startup.com", "Startup LLC", LeadStatus.QUALIFIED);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("Tech", null);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompany()).isEqualTo("Tech Corp");
    }

    @Test
    void shouldSearchByNameOrEmailWhenSearchTextMatchesPartially() {
        // Given
        leadService.addLead("john.doe@example.com", "Tech Corp", LeadStatus.NEW);
        leadService.addLead("jane.doe@example.com", "Biz Inc", LeadStatus.NEW);
        leadService.addLead("bob.wilson@example.com", "Startup LLC", LeadStatus.CONTACTED);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("doe", null);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Lead::getEmail)
                .allMatch(email -> email.toString().contains("doe"));
    }

    @Test
    void shouldSearchByNameOrEmailWhenSearchTextIsCaseInsensitive() {
        // Given
        leadService.addLead("john.doe@example.com", "Tech Corp", LeadStatus.NEW);

        // When
        List<Lead> resultLowercase = leadService.searchByNameOrEmail("john.doe", null);
        List<Lead> resultUppercase = leadService.searchByNameOrEmail("JOHN.DOE", null);
        List<Lead> resultMixed = leadService.searchByNameOrEmail("John.Doe", null);

        // Then
        assertThat(resultLowercase).hasSize(1);
        assertThat(resultUppercase).hasSize(1);
        assertThat(resultMixed).hasSize(1);
    }

    @Test
    void shouldSearchByNameOrEmailWhenSearchTextIsBlank() {
        // Given
        leadService.addLead("john@example.com", "Tech Corp", LeadStatus.NEW);
        leadService.addLead("jane@example.com", "Biz Inc", LeadStatus.CONTACTED);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("", null);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldSearchByNameOrEmailWhenSearchTextIsNull() {
        // Given
        leadService.addLead("john@example.com", "Tech Corp", LeadStatus.NEW);
        leadService.addLead("jane@example.com", "Biz Inc", LeadStatus.CONTACTED);

        // When
        List<Lead> result = leadService.searchByNameOrEmail(null, null);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldSearchByNameOrEmailWhenSearchTextIsBlankWithSpaces() {
        // Given
        leadService.addLead("john@example.com", "Tech Corp", LeadStatus.NEW);
        leadService.addLead("jane@example.com", "Biz Inc", LeadStatus.CONTACTED);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("   ", null);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldSearchByNameOrEmailWhenNoMatchesFound() {
        // Given
        leadService.addLead("john@example.com", "Tech Corp", LeadStatus.NEW);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("nonexistent", null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldSearchByNameOrEmailWithStatusFilter() {
        // Given
        leadService.addLead("john@example.com", "Tech Corp", LeadStatus.NEW);
        leadService.addLead("jane@example.com", "Biz Inc", LeadStatus.NEW);
        leadService.addLead("bob@example.com", "Startup LLC", LeadStatus.CONTACTED);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("example", LeadStatus.NEW);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(lead -> lead.getStatus() == LeadStatus.NEW);
    }

    @Test
    void shouldSearchByNameOrEmailWithStatusFilterAndNoMatches() {
        // Given
        leadService.addLead("john@example.com", "Tech Corp", LeadStatus.NEW);
        leadService.addLead("jane@example.com", "Biz Inc", LeadStatus.CONTACTED);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("example", LeadStatus.QUALIFIED);

        // Then
        assertThat(result).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            "john, john@example.com, Tech Corp, NEW",
            "tech, john@example.com, Tech Corp, NEW",
            "jane, jane@example.com, Biz Inc, CONTACTED",
            "biz, jane@example.com, Biz Inc, CONTACTED"
    })
    void shouldSearchByNameOrEmailParameterizedTests(String searchText, String expectedEmail,
                                                      String expectedCompany, LeadStatus status) {
        // Given
        leadService.addLead("john@example.com", "Tech Corp", LeadStatus.NEW);
        leadService.addLead("jane@example.com", "Biz Inc", LeadStatus.CONTACTED);

        // When
        List<Lead> result = leadService.searchByNameOrEmail(searchText, status);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo(expectedEmail);
        assertThat(result.get(0).getCompany()).isEqualTo(expectedCompany);
    }

    @Test
    void shouldSearchByNameOrEmailWithExactMatchInEmail() {
        // Given
        leadService.addLead("unique@example.com", "Company A", LeadStatus.NEW);
        leadService.addLead("unique2@example.com", "Company B", LeadStatus.NEW);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("unique@example.com", null);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("unique@example.com");
    }

    @Test
    void shouldSearchByNameOrEmailWithExactMatchInCompany() {
        // Given
        leadService.addLead("john@example.com", "UniqueCompanyName", LeadStatus.NEW);
        leadService.addLead("jane@example.com", "Another Company", LeadStatus.NEW);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("UniqueCompanyName", null);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompany()).isEqualTo("UniqueCompanyName");
    }

    @Test
    void shouldSearchByNameOrEmailWhenSearchTextMatchesEmailDomain() {
        // Given
        leadService.addLead("user1@gmail.com", "Company A", LeadStatus.NEW);
        leadService.addLead("user2@yahoo.com", "Company B", LeadStatus.NEW);
        leadService.addLead("user3@gmail.com", "Company C", LeadStatus.NEW);

        // When
        List<Lead> result = leadService.searchByNameOrEmail("gmail.com", null);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Lead::getEmail)
                .allMatch(email -> email.toString().contains("gmail.com"));
    }
}