package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeadControllerTest {

    @Mock
    private LeadService leadService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private LeadController leadController;

    private UUID testId;
    private Lead testLead;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        now = LocalDateTime.now();
        testLead = new Lead("test@example.com", "Test Company", LeadStatus.NEW);
        testLead.setId(testId);
        testLead.setCreatedAt(now);
    }

    // ==================== HOME TESTS ====================

    @Test
    void homeShouldReturnWelcomeMessageWithLeadsCount() {
        // Given
        List<Lead> leads = Arrays.asList(testLead);
        when(leadService.findAll()).thenReturn(leads);

        // When
        String result = leadController.home();

        // Then
        assertThat(result).isEqualTo("Spring Boot CRM is running! Leads count: 1");
        verify(leadService).findAll();
    }

    @Test
    void homeWhenNoLeadsShouldReturnZeroCount() {
        // Given
        when(leadService.findAll()).thenReturn(List.of());

        // When
        String result = leadController.home();

        // Then
        assertThat(result).isEqualTo("Spring Boot CRM is running! Leads count: 0");
        verify(leadService).findAll();
    }

    // ==================== SHOW CREATE FORM TESTS ====================

    @Test
    void showCreateFormShouldAddAttributesAndReturnView() {
        // When
        String viewName = leadController.showCreateForm(model);

        // Then
        verify(model).addAttribute(eq("lead"), any(Lead.class));
        verify(model).addAttribute("statuses", LeadStatus.values());
        assertThat(viewName).isEqualTo("leads/create");
    }

    // ==================== SHOW LEADS TESTS ====================

    @Test
    void showLeadsWithSearchShouldCallSearchByNameOrEmail() {
        // Given
        String search = "test";
        LeadStatus status = LeadStatus.NEW;
        List<Lead> expectedLeads = Arrays.asList(testLead);
        when(leadService.searchByNameOrEmail(search, status)).thenReturn(expectedLeads);

        // When
        String viewName = leadController.showLeads(search, status, model);

        // Then
        verify(leadService).searchByNameOrEmail(search, status);
        verify(leadService, never()).findByStatus(any());
        verify(model).addAttribute("leads", expectedLeads);
        verify(model).addAttribute("search", search);
        verify(model).addAttribute("currentFilter", status);
        assertThat(viewName).isEqualTo("leads/list");
    }

    @Test
    void showLeadsWithEmptySearchShouldCallFindByStatus() {
        // Given
        String search = "";
        LeadStatus status = LeadStatus.CONTACTED;
        List<Lead> expectedLeads = Arrays.asList(testLead);
        when(leadService.findByStatus(status)).thenReturn(expectedLeads);

        // When
        String viewName = leadController.showLeads(search, status, model);

        // Then
        verify(leadService).findByStatus(status);
        verify(leadService, never()).searchByNameOrEmail(any(), any());
        verify(model).addAttribute("leads", expectedLeads);
        verify(model).addAttribute("search", search);
        verify(model).addAttribute("currentFilter", status);
        assertThat(viewName).isEqualTo("leads/list");
    }

    @Test
    void showLeadsWithoutSearchShouldCallFindByStatus() {
        // Given
        LeadStatus status = LeadStatus.QUALIFIED;
        List<Lead> expectedLeads = Arrays.asList(testLead);
        when(leadService.findByStatus(status)).thenReturn(expectedLeads);

        // When
        String viewName = leadController.showLeads(null, status, model);

        // Then
        verify(leadService).findByStatus(status);
        verify(model).addAttribute("leads", expectedLeads);
        verify(model).addAttribute("search", null);
        verify(model).addAttribute("currentFilter", status);
        assertThat(viewName).isEqualTo("leads/list");
    }

    @Test
    void showLeadsWithoutSearchAndStatusShouldCallFindByStatusWithNull() {
        // Given
        when(leadService.findByStatus(null)).thenReturn(Arrays.asList(testLead));

        // When
        String viewName = leadController.showLeads(null, null, model);

        // Then
        verify(leadService).findByStatus(null);
        verify(model).addAttribute(eq("leads"), anyList());
        verify(model).addAttribute("search", null);
        verify(model).addAttribute("currentFilter", null);
        assertThat(viewName).isEqualTo("leads/list");
    }

    // ==================== CREATE LEAD TESTS ====================

    @Test
    void createLeadWithValidDataShouldRedirectToLeads() {
        // Given
        when(bindingResult.hasErrors()).thenReturn(false);
        when(leadService.addLead(any(Lead.class))).thenReturn(testLead);

        // When
        String result = leadController.createLead(testLead, bindingResult, model);

        // Then
        verify(leadService).addLead(testLead);
        verify(model, never()).addAttribute(anyString(), any());
        assertThat(result).isEqualTo("redirect:/leads");
    }

    @Test
    void createLeadWithValidationErrorsShouldReturnCreateForm() {
        // Given
        when(bindingResult.hasErrors()).thenReturn(true);

        // When
        String result = leadController.createLead(testLead, bindingResult, model);

        // Then
        verify(leadService, never()).addLead(any());
        verify(model).addAttribute("statuses", LeadStatus.values());
        assertThat(result).isEqualTo("leads/create");
    }

    @Test
    void createLeadWithDuplicateEmailShouldReturnCreateFormWithError() {
        // Given
        when(bindingResult.hasErrors()).thenReturn(false);
        when(leadService.addLead(any(Lead.class)))
                .thenThrow(new IllegalStateException("Lead with email already exists: test@example.com"));

        // When
        String result = leadController.createLead(testLead, bindingResult, model);

        // Then
        verify(leadService).addLead(testLead);
        verify(model).addAttribute("error", "Lead with email already exists: test@example.com");
        verify(model).addAttribute("statuses", LeadStatus.values());
        assertThat(result).isEqualTo("leads/create");
    }

    // ==================== SHOW EDIT FORM TESTS ====================

    @Test
    void showEditFormWhenLeadExistsShouldReturnEditForm() {
        // Given
        when(leadService.findById(testId)).thenReturn(Optional.of(testLead));

        // When
        String viewName = leadController.showEditForm(testId, model);

        // Then
        verify(model).addAttribute("lead", testLead);
        verify(model).addAttribute("statuses", LeadStatus.values());
        assertThat(viewName).isEqualTo("leads/edit");
    }

    @Test
    void showEditFormWhenLeadNotFoundShouldThrowNotFoundException() {
        // Given
        when(leadService.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> leadController.showEditForm(testId, model))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException responseEx = (ResponseStatusException) ex;
                    assertThat(responseEx.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(responseEx.getReason()).contains("Cannot find lead with id " + testId);
                });
    }

    // ==================== UPDATE LEAD TESTS ====================

    @Test
    void updateLeadWithValidDataShouldRedirectToLeads() {
        // Given
        when(bindingResult.hasErrors()).thenReturn(false);

        // When
        String result = leadController.updateLead(testId, testLead, bindingResult, model);

        // Then
        verify(leadService).update(testId, testLead);
        verify(model, never()).addAttribute(anyString(), any());
        assertThat(result).isEqualTo("redirect:/leads");
    }

    @Test
    void updateLeadWithValidationErrorsShouldReturnEditForm() {
        // Given
        when(bindingResult.hasErrors()).thenReturn(true);

        // When
        String result = leadController.updateLead(testId, testLead, bindingResult, model);

        // Then
        verify(leadService, never()).update(any(), any());
        verify(model).addAttribute("statuses", LeadStatus.values());
        assertThat(result).isEqualTo("leads/edit");
    }

    // ==================== DELETE LEAD TESTS ====================

    @Test
    void deleteLeadShouldCallServiceAndRedirectToLeads() {
        // When
        String result = leadController.deleteLead(testId);

        // Then
        verify(leadService).delete(testId);
        assertThat(result).isEqualTo("redirect:/leads");
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    void showLeadsShouldHandleBlankSearchString() {
        // Given
        String blankSearch = "   ";
        // Исправлено: мокаем searchByNameOrEmail, потому что строка с пробелами не считается пустой
        when(leadService.searchByNameOrEmail(blankSearch, null)).thenReturn(Arrays.asList(testLead));

        // When
        String viewName = leadController.showLeads(blankSearch, null, model);

        // Then
        verify(leadService).searchByNameOrEmail(blankSearch, null);
        verify(leadService, never()).findByStatus(any());
        verify(model).addAttribute(eq("leads"), anyList());
        verify(model).addAttribute("search", blankSearch);
        assertThat(viewName).isEqualTo("leads/list");
    }

    @Test
    void showLeadsWithSearchAndNullStatusShouldCallSearchWithNullStatus() {
        // Given
        String search = "test";
        when(leadService.searchByNameOrEmail(search, null)).thenReturn(Arrays.asList(testLead));

        // When
        String viewName = leadController.showLeads(search, null, model);

        // Then
        verify(leadService).searchByNameOrEmail(search, null);
        verify(model).addAttribute("currentFilter", null);
        assertThat(viewName).isEqualTo("leads/list");
    }
}