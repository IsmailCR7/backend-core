package ru.mentee.power.crm.spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.DealRepository;
import ru.mentee.power.crm.repository.LeadRepository;

@ExtendWith(MockitoExtension.class)
class LeadServiceMockTest {

    @Mock
    private LeadRepository mockRepository;

    @Mock
    private DealRepository mockDealRepository;

    @Mock
    private LeadProcessor mockLeadProcessor;

    private LeadService service;

    @BeforeEach
    void setUp() {
        service = new LeadService(mockRepository, mockDealRepository, mockLeadProcessor);
    }

    @Test
    void shouldCallRepositorySaveWhenAddingNewLead() {
        when(mockRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(mockRepository.save(any(Lead.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Lead result = service.addLead("new@example.com", "Company", LeadStatus.NEW);

        verify(mockRepository, times(1)).save(any(Lead.class));

        assertThat(result.email()).isEqualTo("new@example.com");
    }

    @Test
    void shouldNotCallSaveWhenEmailExists() {
        when(mockRepository.existsByEmail("existing@example.com"))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.addLead("existing@example.com", "New Company", LeadStatus.NEW)
        ).isInstanceOf(IllegalStateException.class);

        verify(mockRepository, never()).save(any(Lead.class));
    }

    @Test
    void shouldCallExistsByEmailBeforeSave() {
        when(mockRepository.existsByEmail(anyString()))
                .thenReturn(false);
        when(mockRepository.save(any(Lead.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.addLead("test@example.com", "Company", LeadStatus.NEW);

        var inOrder = inOrder(mockRepository);
        inOrder.verify(mockRepository).existsByEmail("test@example.com");
        inOrder.verify(mockRepository).save(any(Lead.class));
    }

    @Test
    void shouldCallDeleteWhenLeadExists() {
        UUID id = UUID.randomUUID();

        Lead lead = new Lead(id, "test@example.ru",
                "TestCorp", LeadStatus.NEW);

        when(mockRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(lead));

        service.delete(id);

        verify(mockRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeleteNonExistentLead() {
        when(mockRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(UUID.randomUUID()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdateNonExistentLead() {
        when(mockRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(UUID.randomUUID(),
                new Lead(UUID.randomUUID(), "test@test.ru",
                        "TestCorp", LeadStatus.NEW)))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void shouldFindLeadsWithoutFilter() {
        Lead lead = new Lead(UUID.randomUUID(), "test@test.ru",
                "TestCorp", LeadStatus.NEW);
        Lead secondLead = new Lead(UUID.randomUUID(), "example@example.ru",
                "ExCorp", LeadStatus.CONTACTED);
        List<Lead> leads = new ArrayList<>();
        leads.add(lead);
        leads.add(secondLead);
        when(mockRepository.findAll()).thenReturn(leads);

        List<Lead> result = service.searchLeads(null, null, null, null);
        List<Lead> anotherResult = service.searchLeads("", "", "", null);

        assertThat(result).isEqualTo(leads);
        assertThat(anotherResult).isEqualTo(leads);
    }

    @Test
    void shouldFindLeadsWhenFilteredByEmail() {
        Lead lead = new Lead(UUID.randomUUID(), "test@test.ru",
                "TestCorp", LeadStatus.NEW);
        Lead secondLead = new Lead(UUID.randomUUID(), "example@example.ru",
                "ExCorp", LeadStatus.CONTACTED);
        List<Lead> leads = List.of(lead, secondLead);
        when(mockRepository.findAll()).thenReturn(leads);

        List<Lead> result = service.searchLeads(null, "example", null, null);

        assertThat(result).contains(secondLead);
    }

    @Test
    void shouldFindLeadsWhenFilteredByCompany() {
        Lead lead = new Lead(UUID.randomUUID(), "test@test.ru",
                "TestCorp", LeadStatus.NEW);
        Lead secondLead = new Lead(UUID.randomUUID(), "example@example.ru",
                "ExCorp", LeadStatus.CONTACTED);
        List<Lead> leads = List.of(lead, secondLead);
        when(mockRepository.findAll()).thenReturn(leads);

        List<Lead> result = service.searchLeads(null, null, "ExCorp", null);

        assertThat(result).contains(secondLead);
    }

    @Test
    void shouldFindLeadsWhenFilteredByStatus() {
        Lead lead = new Lead(UUID.randomUUID(), "test@test.ru",
                "TestCorp", LeadStatus.NEW);
        Lead secondLead = new Lead(UUID.randomUUID(), "example@example.ru",
                "ExCorp", LeadStatus.CONTACTED);
        List<Lead> leads = List.of(secondLead);
        when(mockRepository.findByStatus(LeadStatus.CONTACTED)).thenReturn(leads);

        List<Lead> result = service.searchLeads(null, null, null, LeadStatus.CONTACTED);

        assertThat(result).contains(secondLead);
        verify(mockRepository).findByStatus(LeadStatus.CONTACTED);
    }

    @Test
    void shouldFindLeadsWhenFilteredByName() {
        Lead lead = new Lead(UUID.randomUUID(), "Anna", "test@test.ru",
                "TestCorp", LeadStatus.NEW);
        Lead secondLead = new Lead(UUID.randomUUID(), "Batista", "example@example.ru",
                "ExCorp", LeadStatus.CONTACTED);
        List<Lead> leads = List.of(lead, secondLead);
        when(mockRepository.findAll()).thenReturn(leads);

        List<Lead> result = service.searchLeads("Batista", null, null, null);

        assertThat(result).contains(secondLead);
    }

    @Test
    void shouldThrowExceptionWhenAddedLeadWithSameEmail() {
        when(mockRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> service.addLead("Anna", "test@test.ru",
                "TestCorp", LeadStatus.NEW))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldFindByEmailWhenItCalled() {
        Lead lead = new Lead(UUID.randomUUID(), "Anna", "test@test.ru",
                "TestCorp", LeadStatus.NEW);

        when(mockRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(lead));

        Optional<Lead> result = service.findByEmail(lead.email());

        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo(lead.email());
        assertThat(result.get().name()).isEqualTo("Anna");

        verify(mockRepository).findByEmail(lead.email());
    }

    @Test
    void shouldFindByStatusWhenItCalled() {
        Lead firstLead = new Lead(UUID.randomUUID(), "Anna",
                "anna@test.ru", "Corp1", LeadStatus.NEW);
        Lead secondLead = new Lead(UUID.randomUUID(), "Bob",
                "bob@test.ru", "Corp2", LeadStatus.NEW);
        Lead thirdLead = new Lead(UUID.randomUUID(), "Charlie",
                "charlie@test.ru", "Corp3", LeadStatus.CONTACTED);

        when(mockRepository.findByStatus(LeadStatus.NEW))
                .thenReturn(List.of(firstLead, secondLead));

        List<Lead> result = service.findByStatus(LeadStatus.NEW);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(firstLead, secondLead);
        assertThat(result).noneMatch(lead -> lead.status() == LeadStatus.CONTACTED);

        verify(mockRepository).findByStatus(LeadStatus.NEW);
    }

    @Test
    void shouldFindByStatusesWhenItCalled() {
        Lead firstLead = new Lead(UUID.randomUUID(), "Anna",
                "anna@test.ru", "Corp1", LeadStatus.NEW);
        Lead secondLead = new Lead(UUID.randomUUID(), "Charlie",
                "charlie@test.ru", "Corp3", LeadStatus.CONTACTED);

        List<LeadStatus> statuses = List.of(LeadStatus.NEW, LeadStatus.CONTACTED);

        when(mockRepository.findByStatusIn(statuses))
                .thenReturn(List.of(firstLead, secondLead));

        List<Lead> result = service.findByStatuses(LeadStatus.NEW, LeadStatus.CONTACTED);

        verify(mockRepository).findByStatusIn(statuses);
        assertThat(result).containsExactlyInAnyOrder(firstLead, secondLead);
    }
}

