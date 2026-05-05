package ru.mentee.power.crm.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeadServiceMockTest {

    @Mock
    private LeadRepository mockRepository;

    @InjectMocks  // Вместо ручного создания
    private LeadService service;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        // Если используете @InjectMocks, не нужно создавать service вручную
        // service = new LeadService(mockRepository); // Убрать!
    }

    @Test
    void shouldCallRepositorySaveWhenAddingNewLead() {
        // Given
        when(mockRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(mockRepository.save(any(Lead.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Lead result = service.addLead("test@mail.ru", "testCompany", LeadStatus.NEW);

        // Then
        verify(mockRepository, times(1)).save(any(Lead.class));
        assertThat(result.getEmail()).isEqualTo("test@mail.ru"); // Исправлено: email() -> getEmail()
        assertThat(result.getCompany()).isEqualTo("testCompany");
        assertThat(result.getStatus()).isEqualTo(LeadStatus.NEW);
    }

    @Test
    void shouldNotCalledRepositorySaveWhenEmailExist() {
        // Given - исправленный конструктор Lead
        Lead existingLead = new Lead("existing@example.com", "Existing Company", LeadStatus.CONTACTED);
        existingLead.setId(UUID.randomUUID());
        existingLead.setCreatedAt(now);

        when(mockRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(existingLead));

        // When & Then
        assertThatThrownBy(() ->
                service.addLead("existing@example.com", "New Company", LeadStatus.NEW))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Lead with email already exists");

        verify(mockRepository, never()).save(any(Lead.class));
    }

    @Test
    void shouldCalledFindByEmailBeforeSave() {
        // Given
        when(mockRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(mockRepository.save(any(Lead.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        service.addLead("test@example.com", "Company", LeadStatus.NEW);

        // Then
        InOrder inOrder = inOrder(mockRepository);
        inOrder.verify(mockRepository).findByEmail("test@example.com");
        inOrder.verify(mockRepository).save(any(Lead.class));
    }

    @Test
    void shouldReturnLeadWhenFindByEmailExists() {
        // Given
        Lead existingLead = new Lead("find@example.com", "Find Company", LeadStatus.QUALIFIED);
        existingLead.setId(UUID.randomUUID());
        existingLead.setCreatedAt(now);

        when(mockRepository.findByEmail("find@example.com"))
                .thenReturn(Optional.of(existingLead));

        // When
        Optional<Lead> result = service.findByEmail("find@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("find@example.com");
        assertThat(result.get().getCompany()).isEqualTo("Find Company");
        verify(mockRepository, times(1)).findByEmail("find@example.com");
    }

    @Test
    void shouldReturnEmptyWhenFindByEmailNotFound() {
        // Given
        when(mockRepository.findByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        // When
        Optional<Lead> result = service.findByEmail("notfound@example.com");

        // Then
        assertThat(result).isEmpty();
        verify(mockRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    void shouldCallRepositoryDeleteWhenDeletingLead() {
        // Given
        UUID leadId = UUID.randomUUID();
        doNothing().when(mockRepository).deleteById(leadId);
        when(mockRepository.existsById(leadId)).thenReturn(true);

        // When
        service.delete(leadId);

        // Then
        verify(mockRepository, times(1)).deleteById(leadId);
        verify(mockRepository, times(1)).existsById(leadId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentLead() {
        // Given
        UUID leadId = UUID.randomUUID();
        when(mockRepository.existsById(leadId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> service.delete(leadId))
                .isInstanceOf(org.springframework.web.server.ResponseStatusException.class);

        verify(mockRepository, never()).deleteById(any());
    }

    @Test
    void shouldUpdateLeadWhenExists() {
        // Given
        UUID leadId = UUID.randomUUID();
        Lead existingLead = new Lead("old@example.com", "Old Company", LeadStatus.NEW);
        existingLead.setId(leadId);
        existingLead.setCreatedAt(now);

        Lead updateLead = new Lead("new@example.com", "New Company", LeadStatus.QUALIFIED);

        when(mockRepository.findById(leadId)).thenReturn(Optional.of(existingLead));
        when(mockRepository.save(any(Lead.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Lead result = service.update(leadId, updateLead);

        // Then
        assertThat(result.getEmail()).isEqualTo("new@example.com");
        assertThat(result.getCompany()).isEqualTo("New Company");
        assertThat(result.getStatus()).isEqualTo(LeadStatus.QUALIFIED);
        verify(mockRepository, times(1)).save(existingLead);
    }
}
