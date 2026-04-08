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
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
