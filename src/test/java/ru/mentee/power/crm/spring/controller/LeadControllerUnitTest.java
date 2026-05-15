package ru.mentee.power.crm.spring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

@WebMvcTest(LeadController.class)
public class LeadControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeadService leadService;

    // ===== ТЕСТЫ УДАЛЕНИЯ =====

    @Test
    void shouldThrowExceptionWhenDeleteNotExistedLead() throws Exception {
        UUID id = UUID.randomUUID();

        when(leadService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(post("/leads/{id}/delete", id))
                .andExpect(status().is4xxClientError());

        verify(leadService).findById(id);
    }

    @Test
    void shouldDeleteLeadAndRedirect() throws Exception {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "test@example.ru", "TestCorp", LeadStatus.NEW);

        when(leadService.findById(id)).thenReturn(Optional.of(lead));
        doNothing().when(leadService).delete(id);

        mockMvc.perform(post("/leads/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));

        verify(leadService).delete(id);
    }

    // ===== ТЕСТЫ ОБНОВЛЕНИЯ =====

    @Test
    void shouldReturnErrorWhenUpdateWithInvalidData() throws Exception {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "test@example.ru", "TestCorp", LeadStatus.NEW);

        when(leadService.findById(id)).thenReturn(Optional.of(lead));

        mockMvc.perform(post("/leads/" + id)
                        .param("email", "testexample")
                        .param("company", "TestCorp")
                        .param("status", "NEW"))
                .andExpect(view().name("leads/edit"))  // изменено с "leads/form"
                .andExpect(model().attributeHasFieldErrors("lead", "email"));
    }

    // ===== ТЕСТЫ ПОИСКА С ФИЛЬТРАЦИЕЙ =====
    // ВНИМАНИЕ: теперь используем searchLeads() вместо findLeads()

    @Test
    void shouldReturnLeadsWhenFilteredByEmail() throws Exception {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.ru", "TestCorp", LeadStatus.NEW);
        List<Lead> leads = new ArrayList<>();
        leads.add(lead);

        when(leadService.searchLeads(null, "test", null, null))
                .thenReturn(leads);

        mockMvc.perform(get("/leads").param("email", "test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("leads", leads))
                .andExpect(model().attribute("email", "test"));
    }

    @Test
    void shouldReturnLeadsWhenFilteredByStatus() throws Exception {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.ru", "TestCorp", LeadStatus.NEW);
        List<Lead> leads = new ArrayList<>();
        leads.add(lead);

        when(leadService.searchLeads(null, null, null, LeadStatus.NEW))
                .thenReturn(leads);

        mockMvc.perform(get("/leads").param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("leads", leads))
                .andExpect(model().attribute("status", LeadStatus.NEW));
    }

    @Test
    void shouldReturnLeadsWhenFilteredByEmailAndStatus() throws Exception {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.ru", "TestCorp", LeadStatus.NEW);
        List<Lead> leads = new ArrayList<>();
        leads.add(lead);

        when(leadService.searchLeads(null, "test", null, LeadStatus.NEW))
                .thenReturn(leads);

        mockMvc.perform(get("/leads")
                        .param("status", "NEW")
                        .param("email", "test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("leads", leads))
                .andExpect(model().attribute("status", LeadStatus.NEW))
                .andExpect(model().attribute("email", "test"));
    }

    @Test
    void shouldReturnLeadsWhenFilteredByCompany() throws Exception {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.ru", "ACME Corp", LeadStatus.NEW);
        List<Lead> leads = new ArrayList<>();
        leads.add(lead);

        when(leadService.searchLeads(null, null, "ACME", null))
                .thenReturn(leads);

        mockMvc.perform(get("/leads").param("company", "ACME"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("leads", leads))
                .andExpect(model().attribute("company", "ACME"));
    }

    @Test
    void shouldReturnLeadsWhenFilteredByName() throws Exception {
        Lead lead = new Lead(UUID.randomUUID(), "John Doe", "john@example.ru", "TestCorp", LeadStatus.NEW);
        List<Lead> leads = new ArrayList<>();
        leads.add(lead);

        when(leadService.searchLeads("John", null, null, null))
                .thenReturn(leads);

        mockMvc.perform(get("/leads").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("leads", leads))
                .andExpect(model().attribute("name", "John"));
    }

    @Test
    void shouldReturnLeadsWithoutFilter() throws Exception {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.ru", "TestCorp", LeadStatus.NEW);
        List<Lead> leads = new ArrayList<>();
        leads.add(lead);

        when(leadService.searchLeads(null, null, null, null))
                .thenReturn(leads);

        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("leads", leads));
    }

    // ===== ТЕСТЫ СОЗДАНИЯ ЛИДА =====

    @Test
    void shouldReturnFormWithErrorWhenEmailIsBlank() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "")
                        .param("company", "TestCorp")
                        .param("status", "NEW"))
                .andExpect(view().name("leads/create"))  // изменено с "leads/form"
                .andExpect(model().attributeHasFieldErrors("lead", "email"));
    }

    @Test
    void shouldReturnFormWithErrorWhenEmailIsInvalid() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "ololo@ololo")
                        .param("company", "TestCorp")
                        .param("status", "NEW"))
                .andExpect(view().name("leads/create"))  // изменено с "leads/form"
                .andExpect(model().attributeHasFieldErrors("lead", "email"));
    }

    @Test
    void shouldRedirectWhenEmailIsValid() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "test@example.ru")
                        .param("company", "TestCorp")
                        .param("status", "NEW"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/leads"));
    }

    @Test
    void shouldReturnFormWithErrorWhenCompanyIsBlank() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "test@example.ru")
                        .param("company", "")
                        .param("status", "NEW"))
                .andExpect(view().name("leads/create"))  // изменено с "leads/form"
                .andExpect(model().attributeHasFieldErrors("lead", "company"));
    }

    @Test
    void shouldReturnFormWithErrorWhenStatusIsNull() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "test@example.ru")
                        .param("company", "TestCorp"))
                .andExpect(view().name("leads/create"))  // изменено с "leads/form"
                .andExpect(model().attributeHasFieldErrors("lead", "status"));
    }

    // ===== ТЕСТЫ ДОМАШНЕЙ СТРАНИЦЫ =====

    @Test
    void shouldReturnHomePageWithCorrectLeadCount() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Spring Boot CRM is running! Leads in Database: 0 leads."));

    }
    // ===== ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ ДЛЯ НОВЫХ МАРШРУТОВ =====

    @Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/leads/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"))
                .andExpect(model().attributeExists("lead"));
    }

    @Test
    void shouldShowEditFormWhenLeadExists() throws Exception {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "John Doe", "john@example.ru", "ACME Corp", LeadStatus.NEW);

        when(leadService.findById(id)).thenReturn(Optional.of(lead));

        mockMvc.perform(get("/leads/{id}/edit", id))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/edit"))
                .andExpect(model().attribute("lead", lead));
    }

    @Test
    void shouldReturn404WhenEditFormForNonExistentLead() throws Exception {
        UUID id = UUID.randomUUID();

        when(leadService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/leads/{id}/edit", id))
                .andExpect(status().isNotFound());
    }
}
