package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeadController.class)
class LeadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeadService leadService;

    private Lead lead1;
    private Lead lead2;
    private Lead lead3;
    private Lead lead4;
    private Lead lead5;

    @BeforeEach
    void setUp() {
        // Подготовка тестовых данных
        lead1 = new Lead(
                UUID.randomUUID(),
                "ivan@example.com",
                "Ivan Company",
                LeadStatus.NEW
        );

        lead2 = new Lead(
                UUID.randomUUID(),
                "petr@example.com",
                "Petr Company",
                LeadStatus.CONTACTED
        );

        lead3 = new Lead(
                UUID.randomUUID(),
                "ivanov@example.com",
                "Ivanov Corp",
                LeadStatus.QUALIFIED
        );

        lead4 = new Lead(
                UUID.randomUUID(),
                "sergey@example.com",
                "Sergey Company",
                LeadStatus.NEW
        );

        lead5 = new Lead(
                UUID.randomUUID(),
                "test@example.com",
                "Test Company",
                LeadStatus.LOST
        );
    }

    @Test
    void testGetLeadsWithSearchParamReturnsLeadsContainingIvan() throws Exception {
        // Arrange
        List<Lead> expectedLeads = Arrays.asList(lead1, lead3);
        when(leadService.searchByNameOrEmail(eq("ivan"), isNull()))
                .thenReturn(expectedLeads);

        // Act & Assert
        mockMvc.perform(get("/leads")
                        .param("search", "ivan"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attributeExists("leads"))
                .andExpect(model().attribute("leads", hasSize(2)))
                .andExpect(model().attribute("leads", hasItems(lead1, lead3)))
                .andExpect(model().attribute("search", "ivan"))
                .andExpect(model().attribute("currentFilter", nullValue()));
    }

    @Test
    void testGetLeadsWithStatusParamReturnsOnlyLeadsWithStatusNew() throws Exception {
        // Arrange
        List<Lead> expectedLeads = Arrays.asList(lead1, lead4);
        // Когда search == null, вызывается findLeads
        when(leadService.findLeads(isNull(), isNull(), eq(LeadStatus.NEW)))
                .thenReturn(expectedLeads);

        // Act & Assert
        mockMvc.perform(get("/leads")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attributeExists("leads"))
                .andExpect(model().attribute("leads", hasSize(2)))
                .andExpect(model().attribute("leads", hasItems(lead1, lead4)))
                .andExpect(model().attribute("currentFilter", is(LeadStatus.NEW)))
                .andExpect(model().attribute("search", nullValue()));
    }

    @Test
    void testGetLeadsWithoutParamsReturnsAllLeads() throws Exception {
        // Arrange
        List<Lead> allLeads = Arrays.asList(lead1, lead2, lead3, lead4, lead5);
        // Когда search == null и status == null, вызывается findLeads с null параметрами
        when(leadService.findLeads(isNull(), isNull(), isNull()))
                .thenReturn(allLeads);

        // Act & Assert
        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attributeExists("leads"))
                .andExpect(model().attribute("leads", hasSize(5)))
                .andExpect(model().attribute("leads", hasItems(lead1, lead2, lead3, lead4, lead5)))
                .andExpect(model().attribute("search", nullValue()))
                .andExpect(model().attribute("currentFilter", nullValue()));
    }

    @Test
    void testGetLeadsWithSearchAndStatusParamsCombinesBothFilters() throws Exception {
        // Arrange
        List<Lead> expectedLeads = Arrays.asList(lead1);
        // Когда есть search, вызывается searchByNameOrEmail
        when(leadService.searchByNameOrEmail(eq("ivan"), eq(LeadStatus.NEW)))
                .thenReturn(expectedLeads);

        // Act & Assert
        mockMvc.perform(get("/leads")
                        .param("search", "ivan")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attributeExists("leads"))
                .andExpect(model().attribute("leads", hasSize(1)))
                .andExpect(model().attribute("leads", hasItem(lead1)))
                .andExpect(model().attribute("search", "ivan"))
                .andExpect(model().attribute("currentFilter", is(LeadStatus.NEW)));
    }

    @Test
    void testGetLeadsWithEmptySearchReturnsAllLeads() throws Exception {
        // Arrange
        List<Lead> allLeads = Arrays.asList(lead1, lead2, lead3, lead4, lead5);
        // Пустая строка search не считается null, поэтому вызывается searchByNameOrEmail
        when(leadService.findLeads(isNull(), isNull(), isNull()))
                .thenReturn(allLeads);

        // Act & Assert
        mockMvc.perform(get("/leads")
                        .param("search", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attribute("leads", hasSize(5)))
                .andExpect(model().attribute("leads", hasItems(lead1, lead2, lead3, lead4, lead5)))
                .andExpect(model().attribute("search", ""))
                .andExpect(model().attribute("currentFilter", nullValue()));
    }

    @Test
    void testGetLeadsWithNonExistentSearchReturnsEmptyList() throws Exception {
        // Arrange
        when(leadService.searchByNameOrEmail(eq("nonexistent"), isNull()))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/leads")
                        .param("search", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attribute("leads", empty()))
                .andExpect(model().attribute("leads", hasSize(0)))
                .andExpect(model().attribute("search", "nonexistent"))
                .andExpect(model().attribute("currentFilter", nullValue()));
    }

    @Test
    void testGetLeadsWithStatusContactedReturnsOnlyContactedLeads() throws Exception {
        // Arrange
        List<Lead> expectedLeads = Arrays.asList(lead2);
        when(leadService.findLeads(isNull(), isNull(), eq(LeadStatus.CONTACTED)))
                .thenReturn(expectedLeads);

        // Act & Assert
        mockMvc.perform(get("/leads")
                        .param("status", "CONTACTED"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attribute("leads", hasSize(1)))
                .andExpect(model().attribute("leads", hasItem(lead2)))
                .andExpect(model().attribute("currentFilter", is(LeadStatus.CONTACTED)))
                .andExpect(model().attribute("search", nullValue()));
    }

    @Test
    void testGetLeadsWithStatusQualifiedReturnsOnlyQualifiedLeads() throws Exception {
        // Arrange
        List<Lead> expectedLeads = Arrays.asList(lead3);
        when(leadService.findLeads(isNull(), isNull(), eq(LeadStatus.QUALIFIED)))
                .thenReturn(expectedLeads);

        // Act & Assert
        mockMvc.perform(get("/leads")
                        .param("status", "QUALIFIED"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attribute("leads", hasSize(1)))
                .andExpect(model().attribute("leads", hasItem(lead3)))
                .andExpect(model().attribute("currentFilter", is(LeadStatus.QUALIFIED)));
    }

    @Test
    void testGetLeadsWithStatusLostReturnsOnlyLostLeads() throws Exception {
        // Arrange
        List<Lead> expectedLeads = Arrays.asList(lead5);
        when(leadService.findLeads(isNull(), isNull(), eq(LeadStatus.LOST)))
                .thenReturn(expectedLeads);

        // Act & Assert
        mockMvc.perform(get("/leads")
                        .param("status", "LOST"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attribute("leads", hasSize(1)))
                .andExpect(model().attribute("leads", hasItem(lead5)))
                .andExpect(model().attribute("currentFilter", is(LeadStatus.LOST)));
    }
    @Test
    void testCreateLeadWithInvalidEmailShouldReturnFormWithEmailError() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("name", "John Doe")
                        .param("email", "invalidemail")
                        .param("company", "Test Company")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeHasFieldErrorCode("lead", "email", "Email"));
    }

    @Test
    void testCreateLeadWithValidDataShouldRedirectToLeads() throws Exception {
        when(leadService.addLead(any(), any(), any())).thenReturn(new Lead(
                UUID.randomUUID(),
                "John Doe",
                "john@test.com",
                LeadStatus.NEW
        ));

        mockMvc.perform(post("/leads")
                        .param("name", "John Doe")
                        .param("email", "john@test.com")
                        .param("company", "Test Company")
                        .param("status", "NEW"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));
    }

    @Test
    void testCreateLeadWithDuplicateEmailShouldReturnFormWithError() throws Exception {
        doThrow(new IllegalStateException("Lead with this email already exists"))
                .when(leadService).addLead(any(), any(), any());

        mockMvc.perform(post("/leads")
                        .param("name", "John Doe")
                        .param("email", "existing@test.com")
                        .param("company", "Test Company")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("statuses", LeadStatus.values()));
    }

    @Test
    void testShowCreateFormShouldReturnCreateForm() throws Exception {
        mockMvc.perform(get("/leads/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeExists("lead"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void testShowLeadsShouldReturnLeadsList() throws Exception {
        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attributeExists("leads"));
    }

    @Test
    void testShowLeadsWithSearchShouldReturnFilteredLeads() throws Exception {
        mockMvc.perform(get("/leads")
                        .param("search", "test")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/list"))
                .andExpect(model().attributeExists("leads"))
                .andExpect(model().attribute("search", "test"))
                .andExpect(model().attribute("currentFilter", LeadStatus.NEW));
    }

    @Test
    void testShowEditFormWhenLeadExistsShouldReturnEditForm() throws Exception {
        UUID leadId = UUID.randomUUID();
        Lead existingLead = new Lead(leadId, "John Doe", "john@test.com", LeadStatus.NEW);

        when(leadService.findById(leadId)).thenReturn(Optional.of(existingLead));

        mockMvc.perform(get("/leads/{id}/edit", leadId))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/edit"))
                .andExpect(model().attributeExists("lead"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void testShowEditFormWhenLeadNotFoundShouldReturn404() throws Exception {
        UUID leadId = UUID.randomUUID();

        when(leadService.findById(leadId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/leads/{id}/edit", leadId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateLeadWithValidDataShouldRedirectToLeads() throws Exception {
        UUID leadId = UUID.randomUUID();

        mockMvc.perform(post("/leads/{id}", leadId)
                        .param("name", "Updated Name")
                        .param("email", "updated@test.com")
                        .param("company", "Updated Company")
                        .param("status", "CONTACTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));
    }

    @Test
    void testUpdateLeadWithInvalidEmailShouldReturnFormWithError() throws Exception {
        UUID leadId = UUID.randomUUID();

        mockMvc.perform(post("/leads/{id}", leadId)
                        .param("name", "Updated Name")
                        .param("email", "invalidemail")
                        .param("company", "Updated Company")
                        .param("status", "CONTACTED"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/edit"))
                .andExpect(model().attributeHasFieldErrors("lead", "email"));
    }

    @Test
    void testDeleteLeadWhenLeadExistsShouldRedirectToLeads() throws Exception {
        UUID leadId = UUID.randomUUID();

        when(leadService.findById(leadId)).thenReturn(Optional.of(new Lead(
                leadId, "John Doe", "john@test.com", LeadStatus.NEW
        )));

        mockMvc.perform(post("/leads/{id}/delete", leadId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));
    }

    @Test
    void testDeleteLeadWhenLeadNotFoundShouldReturn404() throws Exception {
        UUID leadId = UUID.randomUUID();

        when(leadService.findById(leadId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/leads/{id}/delete", leadId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHomeShouldReturnWelcomeMessage() throws Exception {
        when(leadService.findAll()).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Spring Boot CRM is running! Beans created: 0 leads."));
    }

}
