package ru.mentee.power.crm.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;
import java.util.UUID;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LeadControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LeadRepository leadRepository;

    @BeforeEach
    void setUp() {
        leadRepository.delete(UUID.randomUUID());
    }

    @Test
    void shouldReturnHtmlTableWhenDoGetCalled() throws Exception {
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "Test Company", LeadStatus.NEW);
        leadRepository.save(lead);

        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test@example.com")))
                .andExpect(content().string(containsString("Test Company")));
    }

    @ParameterizedTest
    @CsvSource({
            "NEW",
            "CONTACTED",
            "QUALIFIED"
    })
    void shouldReturnHtmlTableWhenDoGetCalledWithParam(String status) throws Exception {
        Lead lead1 = new Lead(UUID.randomUUID(), "test1@example.com", "Company 1", LeadStatus.NEW);
        Lead lead2 = new Lead(UUID.randomUUID(), "test2@example.com", "Company 2", LeadStatus.CONTACTED);
        Lead lead3 = new Lead(UUID.randomUUID(), "test3@example.com", "Company 3", LeadStatus.QUALIFIED);

        leadRepository.save(lead1);
        leadRepository.save(lead2);
        leadRepository.save(lead3);

        mockMvc.perform(get("/leads?status=" + status))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Список лидов")));
    }

    @Test
    void shouldReturnLeadAddFormWhenDoGetCalled() throws Exception {
        mockMvc.perform(get("/leads/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name=\"email\"")));
    }

    @Test
    void shouldRedirectWhenAddLead() throws Exception {
        mockMvc.perform(post("/leads")
                        .param("email", "test@example.ru")
                        .param("company", "TestCorp")
                        .param("status", "NEW"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/leads"));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        Lead lead = new Lead(UUID.randomUUID(), "test1@example.ru", "TestCorp", LeadStatus.NEW);
        leadRepository.save(lead);

        mockMvc.perform(get("/leads/" + lead.id() + "/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lead"))
                .andExpect(view().name("leads/edit"))
                .andExpect(content().string(containsString("Редактирование лида")))
                .andExpect(content().string(containsString("test1@example.ru")));
    }

    @Test
    void shouldReturn404ForNonexistentId() throws Exception {
        UUID nonexistentId = UUID.randomUUID();
        mockMvc.perform(get("/leads/" + nonexistentId + "/edit"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateLead() throws Exception {
        Lead lead = new Lead(UUID.randomUUID(), "old@example.ru", "TestCorp", LeadStatus.NEW);
        leadRepository.save(lead);

        mockMvc.perform(post("/leads/" + lead.id())
                        .param("id", lead.id().toString())
                        .param("email", "new@example.ru")
                        .param("company", "Updated Corp")  // используем company вместо name
                        .param("phone", "+1234567890")
                        .param("status", "CONTACTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/leads"));

        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("new@example.ru")))
                .andExpect(content().string(containsString("CONTACTED")));
    }
}
