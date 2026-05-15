package ru.mentee.power.crm.spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@ExtendWith(MockitoExtension.class)
class LeadServiceMockTest {

    @Mock
    private LeadRepository mockRepository;

    private LeadService service;

    @BeforeEach
    void setUp() {
        service = new LeadService(mockRepository);
    }


    @Test
    void shouldUseExistsByEmailForUniquenessCheck() {
        // Given
        when(mockRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(mockRepository.save(any(Lead.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        service.addLead("test@example.com", "Company", LeadStatus.NEW);

        // Then
        verify(mockRepository).existsByEmail("test@example.com");
    }

    // ===== ТЕСТЫ УДАЛЕНИЯ =====

    @Test
    void shouldCallDeleteWhenLeadExists() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "test@example.ru", "TestCorp", LeadStatus.NEW);

        when(mockRepository.findById(any(UUID.class))).thenReturn(Optional.of(lead));
        doNothing().when(mockRepository).deleteById(id);

        service.delete(id);

        verify(mockRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeleteNotExistedLead() {
        when(mockRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(UUID.randomUUID()))
                .isInstanceOf(ResponseStatusException.class);
    }

    // ===== ТЕСТЫ ОБНОВЛЕНИЯ =====

    @Test
    void shouldThrowExceptionWhenUpdateNotExistedLead() {
        when(mockRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(UUID.randomUUID(),
                new Lead(UUID.randomUUID(), "test@test.ru", "TestCorp", LeadStatus.NEW)))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void shouldUpdateLeadWhenExists() {
        UUID id = UUID.randomUUID();
        Lead existingLead = new Lead(id, "old@example.ru", "OldCorp", LeadStatus.NEW);
        Lead updatedLead = new Lead(id, "new@example.ru", "NewCorp", LeadStatus.CONTACTED);

        when(mockRepository.findById(id)).thenReturn(Optional.of(existingLead));
        when(mockRepository.save(any(Lead.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Lead result = service.update(id, updatedLead);

        assertThat(result.email()).isEqualTo("new@example.ru");
        assertThat(result.company()).isEqualTo("NewCorp");
        assertThat(result.status()).isEqualTo(LeadStatus.CONTACTED);
    }

    // ===== ТЕСТЫ МЕТОДОВ ПОИСКА =====

    @Test
    void shouldFindByEmailWhenItCalled() {
        Lead lead = new Lead(UUID.randomUUID(), "Anna", "test@test.ru", "TestCorp", LeadStatus.NEW);

        when(mockRepository.findByEmail(any(String.class))).thenReturn(Optional.of(lead));

        Optional<Lead> result = service.findByEmail(lead.email());

        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo(lead.email());
        assertThat(result.get().name()).isEqualTo("Anna");

        verify(mockRepository).findByEmail(lead.email());
    }

    @Test
    void shouldFindByStatusUsingRepository() {
        // Given
        Lead firstLead = new Lead(UUID.randomUUID(), "Anna", "anna@test.ru", "Corp1", LeadStatus.NEW);
        Lead secondLead = new Lead(UUID.randomUUID(), "Bob", "bob@test.ru", "Corp2", LeadStatus.NEW);

        when(mockRepository.findByStatus(LeadStatus.NEW)).thenReturn(List.of(firstLead, secondLead));

        // When
        List<Lead> result = service.findByStatus(LeadStatus.NEW);

        // Then
        assertThat(result).hasSize(2);
        verify(mockRepository).findByStatus(LeadStatus.NEW);
        // Больше не используем findAll()!
    }

    @Test
    void shouldFindByCompany() {
        // Given
        Lead lead = new Lead("test@mail.ru", "ACME Corp", LeadStatus.NEW);
        when(mockRepository.findByCompany("ACME Corp")).thenReturn(List.of(lead));

        // When
        List<Lead> result = service.findByCompany("ACME Corp");

        // Then
        assertThat(result).hasSize(1);
        verify(mockRepository).findByCompany("ACME Corp");
    }

    @Test
    void shouldCountByStatus() {
        // Given
        when(mockRepository.countByStatus(LeadStatus.NEW)).thenReturn(5L);

        // When
        long count = service.countByStatus(LeadStatus.NEW);

        // Then
        assertThat(count).isEqualTo(5L);
        verify(mockRepository).countByStatus(LeadStatus.NEW);
    }

    @Test
    void shouldCheckExistsByEmail() {
        // Given
        when(mockRepository.existsByEmail("existing@test.com")).thenReturn(true);
        when(mockRepository.existsByEmail("new@test.com")).thenReturn(false);

        // When
        boolean exists = service.existsByEmail("existing@test.com");
        boolean notExists = service.existsByEmail("new@test.com");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
        verify(mockRepository, times(2)).existsByEmail(anyString());
    }

    @Test
    void shouldFindByEmailContaining() {
        // Given
        Lead lead = new Lead(UUID.randomUUID(), "john@test.com", "ACME", LeadStatus.NEW);
        when(mockRepository.findByEmailContaining("john")).thenReturn(List.of(lead));

        // When
        List<Lead> result = service.findByEmailContaining("john");

        // Then
        assertThat(result).hasSize(1);
        verify(mockRepository).findByEmailContaining("john");
    }

    @Test
    void shouldFindByStatusAndCompany() {
        // Given
        Lead lead = new Lead("test@mail.ru", "ACME Corp", LeadStatus.NEW);
        when(mockRepository.findByStatusAndCompany(LeadStatus.NEW, "ACME Corp"))
                .thenReturn(List.of(lead));

        // When
        List<Lead> result = service.findByStatusAndCompany(LeadStatus.NEW, "ACME Corp");

        // Then
        assertThat(result).hasSize(1);
        verify(mockRepository).findByStatusAndCompany(LeadStatus.NEW, "ACME Corp");
    }

    @Test
    void shouldFindByStatuses() {
        // Given
        List<LeadStatus> statuses = List.of(LeadStatus.NEW, LeadStatus.CONTACTED);
        Lead lead = new Lead(UUID.randomUUID(), "test@test.com", "ACME", LeadStatus.NEW);
        when(mockRepository.findByStatusIn(statuses)).thenReturn(List.of(lead));

        // When
        List<Lead> result = service.findByStatuses(statuses);

        // Then
        assertThat(result).hasSize(1);
        verify(mockRepository).findByStatusIn(statuses);
    }

    @Test
    void shouldFindCreatedAfter() {
        // Given
        LocalDateTime date = LocalDateTime.now().minusDays(7);
        Lead lead = new Lead(UUID.randomUUID(), "test@test.com", "ACME", LeadStatus.NEW);
        when(mockRepository.findCreatedAfter(date)).thenReturn(List.of(lead));

        // When
        List<Lead> result = service.findCreatedAfter(date);

        // Then
        assertThat(result).hasSize(1);
        verify(mockRepository).findCreatedAfter(date);
    }


    @Test
    void shouldFindByStatusPaged() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Lead> page = new PageImpl<>(List.of(), pageRequest, 0);
        when(mockRepository.findByStatus(LeadStatus.NEW, pageRequest)).thenReturn(page);

        // When
        Page<Lead> result = service.findByStatusPaged(LeadStatus.NEW, 0, 5);

        // Then
        assertThat(result).isNotNull();
        verify(mockRepository).findByStatus(LeadStatus.NEW, pageRequest);
    }

    // ===== ТЕСТЫ BULK ОПЕРАЦИЙ =====

    @Test
    void shouldConvertNewToContacted() {
        // Given
        when(mockRepository.updateStatusBulk(LeadStatus.NEW, LeadStatus.CONTACTED))
                .thenReturn(3);

        // When
        int updated = service.convertNewToContacted();

        // Then
        assertThat(updated).isEqualTo(3);
        verify(mockRepository).updateStatusBulk(LeadStatus.NEW, LeadStatus.CONTACTED);
    }

    @Test
    void shouldDeleteByStatusBulk() {
        // Given
        when(mockRepository.deleteByStatusBulk(LeadStatus.LOST))
                .thenReturn(5);

        // When
        int deleted = service.deleteByStatusBulk(LeadStatus.LOST);

        // Then
        assertThat(deleted).isEqualTo(5);
        verify(mockRepository).deleteByStatusBulk(LeadStatus.LOST);
    }

    // ===== ТЕСТЫ ПОИСКА С ФИЛЬТРАМИ =====

    @Test
    void shouldSearchLeadsWithoutFilter() {
        // Given
        List<Lead> leads = List.of(
                new Lead(UUID.randomUUID(), "test1@test.com", "ACME", LeadStatus.NEW),
                new Lead(UUID.randomUUID(), "test2@test.com", "Corp", LeadStatus.CONTACTED)
        );
        when(mockRepository.findAll()).thenReturn(leads);

        // When
        List<Lead> result = service.searchLeads(null, null, null, null);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldSearchLeadsFilteredByStatus() {
        // Given
        Lead lead = new Lead(UUID.randomUUID(), "test@test.com", "ACME", LeadStatus.NEW);
        when(mockRepository.findByStatus(LeadStatus.NEW)).thenReturn(List.of(lead));

        // When
        List<Lead> result = service.searchLeads(null, null, null, LeadStatus.NEW);

        // Then
        assertThat(result).hasSize(1);
        verify(mockRepository).findByStatus(LeadStatus.NEW);
    }

    // ===== ТЕСТЫ ДЛЯ ВСПОМОГАТЕЛЬНЫХ МЕТОДОВ =====

    @Test
    void shouldThrowExceptionWhenAddingDuplicateEmail() {
        // Given
        when(mockRepository.existsByEmail("duplicate@test.com")).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> service.addLead("duplicate@test.com", "Company", LeadStatus.NEW))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Lead with email already exists");
    }
}