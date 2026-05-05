package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LeadControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LeadRepository leadRepository;

    @BeforeEach
    void setUp() {
        leadRepository.deleteAll();
    }

//    @Test
//    @Transactional  // Только для этого теста
//    void shouldReturnHtmlTableWhenDoGetCalled() throws Exception {
//        // Given
//        Lead lead = new Lead("test@example.com", "Test Company", LeadStatus.NEW);
//        leadRepository.save(lead);
//        // Принудительно сбрасываем изменения в БД
//        leadRepository.flush();
//
//        // When & Then
//        mockMvc.perform(get("/leads"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("test@example.com")));
//    }

    @Test
    void shouldRedirectWhenAddLead() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "test@example.ru")
                        .param("company", "TestCorp")
                        .param("status", "NEW"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/leads"));
    }
}