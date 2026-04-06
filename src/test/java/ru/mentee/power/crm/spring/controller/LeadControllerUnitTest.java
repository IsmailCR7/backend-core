package ru.mentee.power.crm.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.MockLeadService;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeadController.class)
class LeadControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private LeadService leadService;

    @Test
    void shouldCreateControllerWithoutSpring() {
        // Given: mock service без Spring контейнера
        MockLeadService mockService = new MockLeadService();

        // When: создаём контроллер через конструктор (pure Java)
        LeadController controller = new LeadController(mockService);

        // Then: контроллер работает, использует mock service
        String response = controller.home();
        assertThat(response).contains("2 leads"); // MockLeadService возвращает 2 лида
    }

    @Test
    void shouldUseInjectedService() {
        // Given
        MockLeadService mockService = new MockLeadService();
        LeadController controller = new LeadController(mockService);

        // When: вызываем метод контроллера
        String response = controller.home();

        // Then: сервис использован (не null)
        assertThat(response).isNotNull();
        assertThat(response).contains("Spring Boot CRM is running");
    }

    @Test
    void shouldThrowExceptionWhenDeleteNotExistedLead() throws Exception {
        UUID id = UUID.randomUUID();

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
}
