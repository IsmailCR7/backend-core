package ru.mentee.power.crm.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeadServiceMockTest {
    @Mock
    private LeadRepository mockRepository;
    @Mock
    private LeadService service;

    @BeforeEach
    void setUp(){
        service = new LeadService(mockRepository);
    }
    @Test
    void shouldCallRepositorySaveWhenAddingNewLead() {
        when(mockRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(mockRepository.save(any(Lead.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Lead result = service.addLead("test@mail.ru", "testCompany", LeadStatus.NEW);
        verify(mockRepository, times(1)).save(any(Lead.class));
        assertThat(result.email()).isEqualTo("test@mail.ru");

    }
    @Test
    void shouldNotCalledRepositorySaveWhenEmailExist() {
        Lead existingLead = new Lead(
                UUID.randomUUID(),
                "existing@example.com",
                "Existing Company",
                LeadStatus.CONTACTED);
        when(mockRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(existingLead));
        assertThatThrownBy(() ->
                service.addLead("existing@example.com", "New Company", LeadStatus.NEW))
                .isInstanceOf(IllegalStateException.class);
        verify(mockRepository, never()).save(any(Lead.class));
    }
    @Test
    void shouldCalledFindByEmailBeforeSave() {
        when(mockRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(mockRepository.save(any(Lead.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        service.addLead("test@example.com", "Company", LeadStatus.NEW);
        var inOrder = inOrder(mockRepository);
        inOrder.verify(mockRepository).findByEmail("test@example.com");
        inOrder.verify(mockRepository).save(any(Lead.class));
    }

}
